// Run in Blockbench's JavaScript context, in a fresh Java block project.
// Set globalThis.decorConcept to parquet, screen, planter, or shelf first.
(() => {
  const kind = globalThis.decorConcept || 'parquet';
  const palettes = {
    oak: ['#74532f', '#926c3d', '#ab824d', '#bd955e', '#c9a66b'],
    dark: ['#493221', '#62462c', '#795635', '#90673f', '#a0794b'],
    soil: ['#322720', '#453127', '#594032', '#684d38', '#79573d']
  };
  const textures = {};
  function texture(name, pattern, palette = palettes.oak) {
    const canvas = document.createElement('canvas');
    canvas.width = canvas.height = 16;
    const ctx = canvas.getContext('2d');
    for (let y = 0; y < 16; y++) for (let x = 0; x < 16; x++) {
      let index = 2 + ((x * 13 + y * 7 + (x >> 2)) % 7 === 0 ? 1 : 0);
      if (pattern === 'wood') {
        if (y % 4 === 0) index = 1;
        if ((x + (y >> 2) * 7) % 16 === 0 && y % 4 !== 0) index = 0;
        if ((x * 3 + y * 11) % 19 === 0) index = 4;
      } else if (pattern === 'parquet') {
        const horizontal = ((x >> 3) + (y >> 3)) % 2 === 0;
        const along = horizontal ? x % 8 : y % 8;
        const across = horizontal ? y % 8 : x % 8;
        index = across % 3 === 0 || along === 0 ? 0 : across % 3 === 1 ? 3 : 2;
        if (index > 0 && (along + across * 3) % 7 === 0) index = 4;
      } else if (pattern === 'screen') {
        const vertical = x % 4 === 0 || x % 4 === 1;
        const horizontal = y % 4 === 0 || y % 4 === 1;
        if (!vertical && !horizontal) continue;
        index = vertical ? x % 4 === 0 ? 3 : 1 : y % 4 === 0 ? 4 : 2;
        if (vertical && horizontal) index = ((x >> 2) + (y >> 2)) % 2 ? 1 : 3;
      } else index = (x * 17 + y * 13 + x * y) % 5;
      ctx.fillStyle = palette[index];
      ctx.fillRect(x, y, 1, 1);
    }
    const tex = new Texture({name: name + '.png', namespace: 'wooden_accents_concepts', folder: 'block'}).fromDataURL(canvas.toDataURL());
    tex.add(false);
    textures[name] = tex;
    return tex;
  }
  const wood = texture('oak_joinery', 'wood');
  const dark = texture('dark_joinery', 'wood', palettes.dark);
  function cube(name, from, to, tex = wood) {
    const c = new Cube({name, from, to, autouv: 0}).init();
    for (const [face, data] of Object.entries(c.faces)) {
      const axis = face === 'east' || face === 'west' ? 2 : 0;
      const width = to[axis] - from[axis];
      const height = face === 'up' || face === 'down' ? to[2] - from[2] : to[1] - from[1];
      data.texture = tex.uuid;
      data.uv = [0, 0, width, height];
    }
    return c;
  }
  Undo.initEdit({elements: [], textures: [], outliner: true});
  if (kind === 'parquet') {
    const parquet = texture('oak_basketweave', 'parquet');
    const tile = cube('parquet_floor_tile', [0, 0, 0], [16, 1, 16]);
    tile.faces.up.texture = parquet.uuid;
    tile.faces.up.uv = [0, 0, 16, 16];
  } else if (kind === 'screen') {
    const woven = texture('oak_woven_screen', 'screen');
    cube('left_stile', [0, 0, 7], [1, 16, 9]);
    cube('right_stile', [15, 0, 7], [16, 16, 9]);
    cube('bottom_rail', [1, 0, 7], [15, 1, 9]);
    cube('top_rail', [1, 15, 7], [15, 16, 9]);
    const panel = cube('woven_cutout_panel', [1, 1, 8], [15, 15, 8], woven);
    for (const [face, data] of Object.entries(panel.faces)) {
      data.texture = face === 'north' || face === 'south' ? woven.uuid : null;
      data.uv = [1, 1, 15, 15];
    }
  } else if (kind === 'planter') {
    const soil = texture('potting_soil', 'soil', palettes.soil);
    cube('base', [1, 0, 5], [15, 2, 13], dark);
    cube('front_planks', [1, 2, 5], [15, 7, 6]);
    cube('back_planks', [1, 2, 12], [15, 7, 13]);
    cube('left_end', [1, 2, 6], [2, 7, 12]);
    cube('right_end', [14, 2, 6], [15, 7, 12]);
    cube('soil', [2, 2, 6], [14, 5.5, 12], soil);
    cube('front_rim', [0, 7, 4], [16, 8, 6]);
    cube('back_rim', [0, 7, 12], [16, 8, 14]);
    cube('left_rim', [0, 7, 6], [2, 8, 12]);
    cube('right_rim', [14, 7, 6], [16, 8, 12]);
    for (const x of [3, 11]) {
      cube('front_batten_' + x, [x, 1, 4.5], [x + 2, 7, 5], dark);
      cube('foot_' + x, [x, 0, 6], [x + 2, 1, 12], dark);
    }
  } else if (kind === 'shelf') {
    cube('shelf_board', [0, 11, 6], [16, 13, 16]);
    cube('back_lip', [0, 13, 15], [16, 15, 16]);
    for (const x of [2, 12]) {
      cube('wall_plate_' + x, [x, 3, 14], [x + 2, 11, 16], dark);
      cube('corbel_top_' + x, [x, 9, 7], [x + 2, 11, 14], dark);
      cube('corbel_step_' + x, [x, 7, 9], [x + 2, 9, 14], dark);
      cube('corbel_heel_' + x, [x, 5, 11], [x + 2, 7, 14], dark);
    }
  } else throw new Error('Unknown decor concept: ' + kind);
  Project.texture_width = Project.texture_height = 16;
  Project.name = 'wooden_accents_' + kind;
  Cube.selected.empty();
  Undo.finishEdit('Build ' + kind + ' concept');
  Canvas.updateAll();
  globalThis.decorTextures = textures;
  return JSON.stringify({kind, cubes: Cube.all.length, textures: Object.keys(textures)});
})();

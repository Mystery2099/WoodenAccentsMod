"""Compose the saved Blockbench renders and validate the concept exports."""
import json
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parent
FONT = '/usr/share/fonts/dejavu-sans-fonts/DejaVuSans.ttf'
def font(size):
    try:
        return ImageFont.truetype(FONT, size)
    except OSError:
        return ImageFont.truetype('DejaVuSans.ttf', size)

sheet = Image.new('RGB', (1400, 1160), '#19221f')
draw = ImageDraw.Draw(sheet)
draw.text((44, 28), 'WOODEN ACCENTS / DECOR CONCEPTS', font=font(32), fill='#efddba')
draw.text((44, 76), 'Original 16px textures + editable models | Blockbench renders', font=font(19), fill='#aabbad')
items = [
    ('parquet', '01  Basketweave parquet', 'Patterned flooring / 1px thick', 'oak_basketweave'),
    ('screen', '02  Woven wood screen', 'Open partitions and window infill', 'oak_woven_screen'),
    ('planter', '03  Window planter', 'Shallow box with recessed soil', 'oak_joinery'),
    ('shelf', '04  Bracket shelf', 'Wall shelf with stepped corbels', 'dark_joinery'),
]
for i, (name, title, subtitle, texture) in enumerate(items):
    x, y = 32 + (i % 2) * 690, 125 + (i // 2) * 505
    draw.rounded_rectangle((x, y, x + 666, y + 480), radius=14, fill='#28332d')
    draw.text((x + 24, y + 20), title, font=font(25), fill='#efddba')
    draw.text((x + 24, y + 58), subtitle, font=font(18), fill='#b2c0b4')
    render = Image.open(ROOT / f'{name}-preview.png').convert('RGBA')
    render = render.crop(render.getbbox())
    render.thumbnail((470, 315), Image.Resampling.LANCZOS)
    sheet.paste(render, (x + 20 + (470 - render.width) // 2, y + 112 + (315 - render.height) // 2), render)
    swatch = Image.open(ROOT / f'assets/wooden_accents_concepts/textures/block/{texture}.png').convert('RGBA')
    assert swatch.size == (16, 16)
    swatch = swatch.resize((128, 128), Image.Resampling.NEAREST)
    for sy in range(0, 128, 16):
        for sx in range(0, 128, 16):
            color = '#536057' if (sx // 16 + sy // 16) % 2 else '#414d44'
            draw.rectangle((x + 510 + sx, y + 170 + sy, x + 525 + sx, y + 185 + sy), fill=color)
    sheet.paste(swatch, (x + 510, y + 170), swatch)
    draw.text((x + 510, y + 309), '16 x 16 PNG', font=font(16), fill='#b2c0b4')
    model = json.loads((ROOT / f'{name}.json').read_text())
    for reference in model['textures'].values():
        namespace, path = reference.split(':')
        assert (ROOT / 'assets' / namespace / 'textures' / f'{path}.png').is_file(), reference
    for element in model['elements']:
        assert all(0 <= a <= b <= 16 for a, b in zip(element['from'], element['to']))
        for face in element['faces'].values():
            assert face['texture'][1:] in model['textures']
            assert all(0 <= value <= 16 for value in face['uv'])
    print(f'{name}: {len(model["elements"])} elements, textures and UVs valid')
draw.text((44, 1132), 'Concept assets only. Plants, item display, placement and gameplay are not implemented.', font=font(16), fill='#aabbad')
sheet.save(ROOT / 'comparison.png')

import struct
import math
import subprocess
import os

width = 720
height = 1280

# Create RGB buffer initialized with black
# Note: BMP stored bottom-to-top, BGR
pixels = bytearray(width * height * 3)

def set_pixel(x, y, r, g, b, alpha=1.0):
    if 0 <= x < width and 0 <= y < height:
        idx = (y * width + x) * 3
        if alpha >= 1.0:
            pixels[idx] = int(max(0, min(255, b)))
            pixels[idx+1] = int(max(0, min(255, g)))
            pixels[idx+2] = int(max(0, min(255, r)))
        elif alpha > 0:
            cb, cg, cr = pixels[idx], pixels[idx+1], pixels[idx+2]
            pixels[idx] = int(cb * (1 - alpha) + b * alpha)
            pixels[idx+1] = int(cg * (1 - alpha) + g * alpha)
            pixels[idx+2] = int(cr * (1 - alpha) + r * alpha)

def blend_pixel(x, y, r, g, b, alpha):
    if 0 <= x < width and 0 <= y < height and alpha > 0:
        idx = (y * width + x) * 3
        cb, cg, cr = pixels[idx], pixels[idx+1], pixels[idx+2]
        a = min(1.0, alpha)
        pixels[idx] = int(min(255, cb * (1 - a) + b * a))
        pixels[idx+1] = int(min(255, cg * (1 - a) + g * a))
        pixels[idx+2] = int(min(255, cr * (1 - a) + r * a))

print("Rendering sky and background hills...")
# 1. Sky & Background
for y in range(height):
    ynorm = y / float(height) # 0 is bottom, 1 is top in BMP coords
    # in image coords, top is ynorm = 1.0
    for x in range(width):
        xnorm = x / float(width)
        
        if ynorm > 0.60: # Sky area
            # Gradient from deep blue at very top to warm cyan/gold near horizon
            sky_t = (ynorm - 0.60) / 0.40
            r = int(28 + sky_t * 5 + (1 - sky_t) * 160)
            g = int(105 + sky_t * 20 + (1 - sky_t) * 195)
            b = int(175 + sky_t * 40 + (1 - sky_t) * 230)
            set_pixel(x, y, r, g, b)
        else: # Rolling hills base
            hill_t = ynorm / 0.60
            r = int(140 + hill_t * 70)
            g = int(95 + hill_t * 60)
            b = int(25 + hill_t * 20)
            set_pixel(x, y, r, g, b)

# 2. Rolling hills curves & lush green groves
for x in range(width):
    xn = x / float(width)
    # Hill 1 profile (distant)
    h1 = 0.68 + 0.04 * math.sin(xn * 5.0) + 0.02 * math.cos(xn * 3.0)
    # Hill 2 profile (mid ground)
    h2 = 0.60 + 0.05 * math.sin(xn * 3.8 + 1.2) - 0.03 * math.cos(xn * 7.0)
    # Hill 3 profile (foreground slope)
    h3 = 0.52 + 0.06 * math.sin(xn * 2.5 + 2.0)

    for y in range(height):
        yn = y / float(height)
        if yn < h1 and yn >= h2:
            # Distant golden-green hill
            depth = (h1 - yn) / (h1 - h2 + 0.001)
            r = int(180 + 35 * math.sin(xn * 20) - depth * 20)
            g = int(145 + 25 * math.sin(xn * 20) - depth * 15)
            b = int(60 + 15 * math.sin(xn * 20) - depth * 10)
            set_pixel(x, y, r, g, b)
        elif yn < h2 and yn >= h3:
            # Mid golden slope
            depth = (h2 - yn) / (h2 - h3 + 0.001)
            r = int(210 + 20 * math.sin(xn * 15 + yn * 30) - depth * 30)
            g = int(160 + 20 * math.sin(xn * 15 + yn * 30) - depth * 25)
            b = int(70 - depth * 20)
            set_pixel(x, y, r, g, b)

# 3. Green tree rows & hedges along hilltops
for i in range(40):
    tx = int((i * 19) % width)
    txn = tx / float(width)
    h_base = int((0.60 + 0.05 * math.sin(txn * 3.8 + 1.2) - 0.03 * math.cos(txn * 7.0)) * height)
    tree_rad = 12 + (i % 4) * 4
    for dy in range(-tree_rad, tree_rad):
        for dx in range(-tree_rad, tree_rad):
            if dx*dx + dy*dy <= tree_rad*tree_rad:
                blend_pixel(tx + dx, h_base + dy, 35 + (i%5)*8, 85 + (i%7)*10, 30 + (i%3)*6, 0.9)

# 4. Rustic Farmhouse & Barn (left side slope)
house_x = int(width * 0.16)
house_y = int(height * 0.58)
# House stone walls
for dy in range(-16, 16):
    for dx in range(-32, 32):
        blend_pixel(house_x + dx, house_y + dy, 150 + (dx % 4)*10, 130 + (dy % 3)*8, 110, 0.95)
# House terracotta roof
for dy in range(0, 22):
    row_w = int(36 * (1.0 - dy / 22.0))
    for dx in range(-row_w, row_w):
        blend_pixel(house_x + dx, house_y + 16 + dy, 175 - dy*2, 55, 35, 0.95)

# Barn adjacent
barn_x = house_x + 52
barn_y = house_y - 2
for dy in range(-14, 14):
    for dx in range(-20, 20):
        blend_pixel(barn_x + dx, barn_y + dy, 130, 110, 95, 0.95)
for dy in range(0, 18):
    row_w = int(24 * (1.0 - dy / 18.0))
    for dx in range(-row_w, row_w):
        blend_pixel(barn_x + dx, barn_y + 14 + dy, 160, 48, 30, 0.95)

# 5. S-Curve Country Farm Road
road_pts = []
for t in range(100):
    prog = t / 100.0
    # S curve path from farm down to lower field
    rx = house_x + 25 + int(prog * 180 + 60 * math.sin(prog * 4.2))
    ry = house_y - int(prog * 240)
    road_pts.append((rx, ry))

for rx, ry in road_pts:
    road_w = 8 + int((house_y - ry) * 0.05)
    for dy in range(-3, 4):
        for dx in range(-road_w, road_w):
            blend_pixel(rx + dx, ry + dy, 175, 140, 95, 0.85)
            if abs(dx) == int(road_w * 0.5): # Wheel track line
                blend_pixel(rx + dx, ry + dy, 135, 100, 60, 0.6)

# 6. Foreground Wheat Field & Dense Golden Texture
print("Rendering dense golden wheat stalks & awns...")
for y in range(0, int(height * 0.52)):
    yn = y / float(height)
    # Golden base layer with lighting gradient
    light_f = (y / (height * 0.52))
    for x in range(width):
        xn = x / float(width)
        noise = (math.sin(x * 0.8 + y * 0.2) + math.sin(x * 0.3 - y * 0.9)) * 0.5
        r = int(185 + light_f * 45 + noise * 25)
        g = int(120 + light_f * 40 + noise * 20)
        b = int(30 + light_f * 20 + noise * 10)
        set_pixel(x, y, r, g, b)

# Thousands of individual crisp wheat heads and ears in foreground
import random
random.seed(42)
for i in range(1400):
    wx = random.randint(0, width - 1)
    wy = random.randint(0, int(height * 0.50))
    w_len = 16 + int((wy / (height * 0.50)) * 26)
    
    # Golden grain colors
    gr = 220 + random.randint(-20, 35)
    gg = 160 + random.randint(-25, 45)
    gb = 45 + random.randint(-15, 40)
    
    sway = int(math.sin(wy * 0.05 + wx * 0.02) * 8)
    for seg in range(w_len):
        px = wx + int(seg * 0.3) + (seg * sway // w_len)
        py = wy + seg
        blend_pixel(px, py, gr, gg, gb, 0.9)
        # Side awns
        if seg % 3 == 0:
            blend_pixel(px - 3, py + 2, min(255, gr + 20), min(255, gg + 20), gb, 0.7)
            blend_pixel(px + 3, py + 2, min(255, gr + 20), min(255, gg + 20), gb, 0.7)

# 7. Brilliant Sunburst & Radiant Golden Sunbeams from Top Right
print("Rendering sunburst and rays...")
sun_x = int(width * 0.88)
sun_y = int(height * 0.84)

# Sunbeams radiating outward
ray_angles = [math.radians(deg) for deg in range(100, 270, 6)]
for ang in ray_angles:
    for dist in range(10, 850, 2):
        bx = int(sun_x + math.cos(ang) * dist)
        by = int(sun_y - math.sin(ang) * dist)
        if 0 <= bx < width and 0 <= by < height:
            falloff = max(0.0, 1.0 - (dist / 850.0))
            beam_a = falloff * 0.28
            blend_pixel(bx, by, 255, 245, 180, beam_a)

# Sun Core & Glowing Corona
for dy in range(-120, 120):
    for dx in range(-120, 120):
        dist = math.sqrt(dx*dx + dy*dy)
        if dist < 120:
            glow = max(0.0, 1.0 - dist / 120.0)
            blend_pixel(sun_x + dx, sun_y + dy, 255, 250, int(200 + glow * 55), glow * 0.85)

for dy in range(-35, 35):
    for dx in range(-35, 35):
        dist = math.sqrt(dx*dx + dy*dy)
        if dist < 35:
            blend_pixel(sun_x + dx, sun_y + dy, 255, 255, 245, 1.0)

# 8. Kisan Market Circular Logo Badge at Top Center
print("Rendering Kisan Market circular logo emblem...")
badge_cx = int(width * 0.50)
badge_cy = int(height * 0.80) # in top portion
badge_r = 74

# Badge outer shadow & green rim
for dy in range(-badge_r - 6, badge_r + 7):
    for dx in range(-badge_r - 6, badge_r + 7):
        dist = math.sqrt(dx*dx + dy*dy)
        if dist <= badge_r + 5 and dist >= badge_r - 2: # Green border
            set_pixel(badge_cx + dx, badge_cy + dy, 46, 125, 50) # #2E7D32
        elif dist < badge_r - 2: # White background
            set_pixel(badge_cx + dx, badge_cy + dy, 250, 252, 248)

# Sun rays inside badge
for a_deg in range(0, 360, 40):
    rad = math.radians(a_deg)
    for d in range(8, 22):
        sx = int(badge_cx + math.cos(rad) * d)
        sy = int(badge_cy + 14 + math.sin(rad) * d)
        blend_pixel(sx, sy, 245, 158, 11, 0.9)

# Golden sun inside badge
for dy in range(-10, 11):
    for dx in range(-10, 11):
        if dx*dx + dy*dy <= 100:
            set_pixel(badge_cx + dx, badge_cy + 14 + dy, 251, 191, 36)

# Sprout Green Leaves inside badge
for dy in range(-20, 8):
    for dx in range(-18, 19):
        # Left leaf
        if (dx + 8)**2 + (dy + 4)**2 <= 64:
            set_pixel(badge_cx + dx, badge_cy + 8 + dy, 46, 125, 50)
        # Right leaf
        if (dx - 8)**2 + (dy + 4)**2 <= 64:
            set_pixel(badge_cx + dx, badge_cy + 8 + dy, 67, 160, 71)

# Market Stall Awning inside badge
for dy in range(-12, -2):
    for dx in range(-18, 19):
        set_pixel(badge_cx + dx, badge_cy + dy, 141, 110, 99)

# Save to BMP
print("Saving BMP file...")
bmp_path = "/tmp/welcome_bg_farm.bmp"
header = struct.pack('<2sIHHI', b'BM', 54 + len(pixels), 0, 0, 54)
info = struct.pack('<IIIHHIIIIII', 40, width, height, 1, 24, 0, len(pixels), 2835, 2835, 0, 0)
with open(bmp_path, 'wb') as f:
    f.write(header + info + pixels)

print("Converting to PNG at app/src/main/res/drawable/welcome_bg_photo.png...")
os.makedirs("app/src/main/res/drawable", exist_ok=True)
png_path = "app/src/main/res/drawable/welcome_bg_photo.png"
subprocess.run(["ffmpeg", "-y", "-i", bmp_path, png_path], check=True)
print("Done! Generated:", png_path)

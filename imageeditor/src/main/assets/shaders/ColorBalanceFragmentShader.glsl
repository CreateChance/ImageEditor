/**
 * Color balance adjustment fragment shader.
 * Copy from: https://gist.github.com/liovch/3168961
 */

varying highp vec2 v_TextureCoordinates;

uniform sampler2D u_InputTexture;
uniform lowp float u_RedShift;
uniform lowp float u_GreenShift;
uniform lowp float u_BlueShift;

lowp float RGBToL(lowp vec3 color)
{
    lowp float fmin = min(min(color.r, color.g), color.b);    //Min. value of RGB
    lowp float fmax = max(max(color.r, color.g), color.b);    //Max. value of RGB

    return (fmax + fmin) / 2.0; // Luminance
}

lowp vec3 RGBToHSL(lowp vec3 color)
{
    lowp vec3 hsl; // init to 0 to avoid warnings ? (and reverse if + remove first part)

    lowp float fmin = min(min(color.r, color.g), color.b);    //Min. value of RGB
    lowp float fmax = max(max(color.r, color.g), color.b);    //Max. value of RGB
    lowp float delta = fmax - fmin;             //Delta RGB value

    hsl.z = (fmax + fmin) / 2.0; // Luminance

    if (delta == 0.0)		//This is a gray, no chroma...
    {
        hsl.x = 0.0;	// Hue
        hsl.y = 0.0;	// Saturation
    }
    else                                    //Chromatic data...
    {
        if (hsl.z < 0.5)
            hsl.y = delta / (fmax + fmin); // Saturation
        else
            hsl.y = delta / (2.0 - fmax - fmin); // Saturation

        lowp float deltaR = (((fmax - color.r) / 6.0) + (delta / 2.0)) / delta;
        lowp float deltaG = (((fmax - color.g) / 6.0) + (delta / 2.0)) / delta;
        lowp float deltaB = (((fmax - color.b) / 6.0) + (delta / 2.0)) / delta;

        if (color.r == fmax )
            hsl.x = deltaB - deltaG; // Hue
        else if (color.g == fmax)
            hsl.x = (1.0 / 3.0) + deltaR - deltaB; // Hue
        else if (color.b == fmax)
            hsl.x = (2.0 / 3.0) + deltaG - deltaR; // Hue

        if (hsl.x < 0.0)
            hsl.x += 1.0; // Hue
        else if (hsl.x > 1.0)
            hsl.x -= 1.0; // Hue
    }

    return hsl;
}

lowp float HueToRGB(lowp float f1, lowp float f2, lowp float hue)
{
    if (hue < 0.0)
        hue += 1.0;
    else if (hue > 1.0)
        hue -= 1.0;
    lowp float res;
    if ((6.0 * hue) < 1.0)
        res = f1 + (f2 - f1) * 6.0 * hue;
    else if ((2.0 * hue) < 1.0)
        res = f2;
    else if ((3.0 * hue) < 2.0)
        res = f1 + (f2 - f1) * ((2.0 / 3.0) - hue) * 6.0;
    else
        res = f1;
    return res;
}

lowp vec3 HSLToRGB(lowp vec3 hsl)
{
    lowp vec3 rgb;

    if (hsl.y == 0.0)
        rgb = vec3(hsl.z); // Luminance
    else
    {
        lowp float f2;

        if (hsl.z < 0.5)
            f2 = hsl.z * (1.0 + hsl.y);
        else
            f2 = (hsl.z + hsl.y) - (hsl.y * hsl.z);

        lowp float f1 = 2.0 * hsl.z - f2;

        rgb.r = HueToRGB(f1, f2, hsl.x + (1.0/3.0));
        rgb.g = HueToRGB(f1, f2, hsl.x);
        rgb.b= HueToRGB(f1, f2, hsl.x - (1.0/3.0));
    }

    return rgb;
}

void main()
{
    lowp vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);

    // Old way:
//    lowp vec3 newColor = (textureColor.rgb - 0.5) * 2.0;
//    newColor.r = 2.0/3.0 * (1.0 - (newColor.r * newColor.r));
//    newColor.g = 2.0/3.0 * (1.0 - (newColor.g * newColor.g));
//    newColor.b = 2.0/3.0 * (1.0 - (newColor.b * newColor.b));
//
//    newColor.r = clamp(textureColor.r + u_RedShift * newColor.r, 0.0, 1.0);
//    newColor.g = clamp(textureColor.g + u_GreenShift * newColor.g, 0.0, 1.0);
//    newColor.b = clamp(textureColor.b + u_BlueShift * newColor.b, 0.0, 1.0);
//
//    // preserve luminosity
//    lowp vec3 newHSL = RGBToHSL(newColor);
//    lowp float oldLum = RGBToL(textureColor.rgb);
//    textureColor.rgb = HSLToRGB(vec3(newHSL.x, newHSL.y, oldLum));
//
//    gl_FragColor = textureColor;

    // New way:
    lowp float lightness = RGBToL(textureColor.rgb);
    lowp vec3 shift = vec3(u_RedShift, u_GreenShift, u_BlueShift);

    const lowp float a = 0.25;
    const lowp float b = 0.333;
    const lowp float scale = 0.7;

    lowp vec3 midtones = (clamp((lightness - b) /  a + 0.5, 0.0, 1.0) * clamp ((lightness + b - 1.0) / -a + 0.5, 0.0, 1.0) * scale) * shift;

    lowp vec3 newColor = textureColor.rgb + midtones;
    newColor = clamp(newColor, 0.0, 1.0);

    // preserve luminosity
    lowp vec3 newHSL = RGBToHSL(newColor);
    lowp float oldLum = RGBToL(textureColor.rgb);
    textureColor.rgb = HSLToRGB(vec3(newHSL.x, newHSL.y, oldLum));

    gl_FragColor = textureColor;
}
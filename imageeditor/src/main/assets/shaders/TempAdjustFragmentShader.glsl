varying highp vec2 v_TextureCoordinates;

uniform sampler2D u_InputTexture;
uniform float u_Temperature;

const vec3 kWarmFilter = vec3(0.93, 0.54, 0.0);
vec4 temperatureAdjust(vec4 color)
{
    vec3 processed = vec3((color.r < 0.5 ? (2.0 * color.r * kWarmFilter.r) : (1.0 - 2.0 * (1.0 - color.r) * (1.0 - kWarmFilter.r))),
                          (color.g < 0.5 ? (2.0 * color.g * kWarmFilter.g) : (1.0 - 2.0 * (1.0 - color.g) * (1.0 - kWarmFilter.g))),
                          (color.b < 0.5 ? (2.0 * color.b * kWarmFilter.b) : (1.0 - 2.0 * (1.0 - color.b) * (1.0 - kWarmFilter.b))));
    return vec4(mix(color.rgb, processed, u_Temperature), color.a);
}

void main()
{
    vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);
    gl_FragColor = temperatureAdjust(textureColor);
}
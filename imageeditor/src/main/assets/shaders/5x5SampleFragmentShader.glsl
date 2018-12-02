precision highp float;

uniform sampler2D u_InputTexture;

uniform mediump float u_SampleKernel[25];

varying vec2 v_SampleCoordinates[25];

void main()
{
    vec3 color0 = texture2D(u_InputTexture, v_SampleCoordinates[0]).rgb;
    vec3 color1 = texture2D(u_InputTexture, v_SampleCoordinates[1]).rgb;
    vec3 color2 = texture2D(u_InputTexture, v_SampleCoordinates[2]).rgb;
    vec3 color3 = texture2D(u_InputTexture, v_SampleCoordinates[3]).rgb;
    vec3 color4 = texture2D(u_InputTexture, v_SampleCoordinates[4]).rgb;
    vec3 color5 = texture2D(u_InputTexture, v_SampleCoordinates[5]).rgb;
    vec3 color6 = texture2D(u_InputTexture, v_SampleCoordinates[6]).rgb;
    vec3 color7 = texture2D(u_InputTexture, v_SampleCoordinates[7]).rgb;
    vec3 color8 = texture2D(u_InputTexture, v_SampleCoordinates[8]).rgb;
    vec3 color9 = texture2D(u_InputTexture, v_SampleCoordinates[9]).rgb;
    vec3 color10 = texture2D(u_InputTexture, v_SampleCoordinates[10]).rgb;
    vec3 color11 = texture2D(u_InputTexture, v_SampleCoordinates[11]).rgb;
    vec4 color12 = texture2D(u_InputTexture, v_SampleCoordinates[12]);
    vec3 color13 = texture2D(u_InputTexture, v_SampleCoordinates[13]).rgb;
    vec3 color14 = texture2D(u_InputTexture, v_SampleCoordinates[14]).rgb;
    vec3 color15 = texture2D(u_InputTexture, v_SampleCoordinates[15]).rgb;
    vec3 color16 = texture2D(u_InputTexture, v_SampleCoordinates[16]).rgb;
    vec3 color17 = texture2D(u_InputTexture, v_SampleCoordinates[17]).rgb;
    vec3 color18 = texture2D(u_InputTexture, v_SampleCoordinates[18]).rgb;
    vec3 color19 = texture2D(u_InputTexture, v_SampleCoordinates[19]).rgb;
    vec3 color20 = texture2D(u_InputTexture, v_SampleCoordinates[20]).rgb;
    vec3 color21 = texture2D(u_InputTexture, v_SampleCoordinates[21]).rgb;
    vec3 color22 = texture2D(u_InputTexture, v_SampleCoordinates[22]).rgb;
    vec3 color23 = texture2D(u_InputTexture, v_SampleCoordinates[23]).rgb;
    vec3 color24 = texture2D(u_InputTexture, v_SampleCoordinates[24]).rgb;

    vec3 resultColor = color0 * u_SampleKernel[0] +
                        color1 * u_SampleKernel[1] +
                        color2 * u_SampleKernel[2] +
                        color3 * u_SampleKernel[3] +
                        color4 * u_SampleKernel[4] +
                        color5 * u_SampleKernel[5] +
                        color6 * u_SampleKernel[6] +
                        color7 * u_SampleKernel[7] +
                        color8 * u_SampleKernel[8] +
                        color9 * u_SampleKernel[9] +
                        color10 * u_SampleKernel[10] +
                        color11 * u_SampleKernel[11] +
                        color12.rgb * u_SampleKernel[12] +
                        color13 * u_SampleKernel[13] +
                        color14 * u_SampleKernel[14] +
                        color15 * u_SampleKernel[15] +
                        color16 * u_SampleKernel[16] +
                        color17 * u_SampleKernel[17] +
                        color18 * u_SampleKernel[18] +
                        color19 * u_SampleKernel[19] +
                        color20 * u_SampleKernel[20] +
                        color21 * u_SampleKernel[21] +
                        color22 * u_SampleKernel[22] +
                        color23 * u_SampleKernel[23] +
                        color24 * u_SampleKernel[24];

    gl_FragColor = vec4(resultColor, color12.a);
}
attribute vec4 a_Position;
attribute vec4 a_TextureCoordinates;

uniform highp float u_WidthStep;
uniform highp float u_HeightStep;

varying vec2 v_SampleCoordinates[25];

void main()
{
    gl_Position = a_Position;

    v_SampleCoordinates[0] = vec2(a_TextureCoordinates.x - u_WidthStep * 2.0, a_TextureCoordinates.y + u_HeightStep * 2.0);
    v_SampleCoordinates[1] = vec2(a_TextureCoordinates.x - u_WidthStep, a_TextureCoordinates.y + u_HeightStep * 2.0);
    v_SampleCoordinates[2] = vec2(a_TextureCoordinates.x , a_TextureCoordinates.y + u_HeightStep * 2.0);
    v_SampleCoordinates[3] = vec2(a_TextureCoordinates.x + u_WidthStep, a_TextureCoordinates.y + u_WidthStep * 2.0);
    v_SampleCoordinates[4] = vec2(a_TextureCoordinates.x + u_WidthStep * 2.0, a_TextureCoordinates.y + u_HeightStep * 2.0);

    v_SampleCoordinates[5] = vec2(a_TextureCoordinates.x - u_WidthStep * 2.0, a_TextureCoordinates.y + u_HeightStep);
    v_SampleCoordinates[6] = vec2(a_TextureCoordinates.x - u_WidthStep, a_TextureCoordinates.y + u_HeightStep);
    v_SampleCoordinates[7] = vec2(a_TextureCoordinates.x, a_TextureCoordinates.y + u_HeightStep);
    v_SampleCoordinates[8] = vec2(a_TextureCoordinates.x + u_WidthStep * 2.0, a_TextureCoordinates.y + u_HeightStep);
    v_SampleCoordinates[9] = vec2(a_TextureCoordinates.x + u_WidthStep, a_TextureCoordinates.y + u_HeightStep);

    v_SampleCoordinates[10] = vec2(a_TextureCoordinates.x - u_WidthStep * 2.0, a_TextureCoordinates.y);
    v_SampleCoordinates[11] = vec2(a_TextureCoordinates.x - u_WidthStep, a_TextureCoordinates.y);
    v_SampleCoordinates[12] = vec2(a_TextureCoordinates.x, a_TextureCoordinates.y);
    v_SampleCoordinates[13] = vec2(a_TextureCoordinates.x + u_WidthStep, a_TextureCoordinates.y);
    v_SampleCoordinates[14] = vec2(a_TextureCoordinates.x + u_WidthStep * 2.0, a_TextureCoordinates.y);

    v_SampleCoordinates[15] = vec2(a_TextureCoordinates.x - u_WidthStep * 2.0, a_TextureCoordinates.y - u_HeightStep);
    v_SampleCoordinates[16] = vec2(a_TextureCoordinates.x - u_WidthStep, a_TextureCoordinates.y - u_HeightStep);
    v_SampleCoordinates[17] = vec2(a_TextureCoordinates.x, a_TextureCoordinates.y - u_HeightStep);
    v_SampleCoordinates[18] = vec2(a_TextureCoordinates.x + u_WidthStep * 2.0, a_TextureCoordinates.y - u_HeightStep);
    v_SampleCoordinates[19] = vec2(a_TextureCoordinates.x + u_WidthStep, a_TextureCoordinates.y - u_HeightStep);

    v_SampleCoordinates[20] = vec2(a_TextureCoordinates.x - u_WidthStep * 2.0, a_TextureCoordinates.y - u_HeightStep * 2.0);
    v_SampleCoordinates[21] = vec2(a_TextureCoordinates.x - u_WidthStep, a_TextureCoordinates.y - u_HeightStep * 2.0);
    v_SampleCoordinates[22] = vec2(a_TextureCoordinates.x , a_TextureCoordinates.y - u_HeightStep * 2.0);
    v_SampleCoordinates[23] = vec2(a_TextureCoordinates.x + u_WidthStep, a_TextureCoordinates.y - u_WidthStep * 2.0);
    v_SampleCoordinates[24] = vec2(a_TextureCoordinates.x + u_WidthStep * 2.0, a_TextureCoordinates.y - u_HeightStep * 2.0);
}
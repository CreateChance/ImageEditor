uniform sampler2D u_InputTexture;
varying highp vec2 v_TextureCoordinates;

uniform lowp float u_Gamma;

void main()
{
    vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);
    gl_FragColor = vec4(pow(textureColor.rgb, vec3(u_Gamma)), textureColor.a);
}
uniform sampler2D u_InputTexture;
uniform lowp float u_Brightness;
varying highp vec2 v_TextureCoordinates;

void main()
{
    lowp vec4 textureColor = texture2D(u_InputTexture, v_TextureCoordinates);
    gl_FragColor = vec4((textureColor.rgb + vec3(u_Brightness)), textureColor.w);
}
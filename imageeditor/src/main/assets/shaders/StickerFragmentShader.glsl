precision mediump float;
uniform sampler2D u_InputTexture;
uniform float u_AlphaFactor;
uniform vec3 u_StickerColor;
varying vec2 v_TextureCoordinates;
void main() {
    mediump vec4 sampledColor = texture2D(u_InputTexture, v_TextureCoordinates);
    gl_FragColor = vec4(u_StickerColor, u_AlphaFactor) * sampledColor;
}
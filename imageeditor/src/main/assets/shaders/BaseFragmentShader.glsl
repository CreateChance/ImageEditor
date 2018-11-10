precision mediump float;

uniform sampler2D u_InputTexture;
varying vec2 v_TextureCoordinates;

void main() {
    gl_FragColor = texture2D(u_InputTexture, v_TextureCoordinates);
}

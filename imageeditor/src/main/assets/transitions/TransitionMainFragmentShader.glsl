/**
 * Transition main fragment shader.
 * author: createchance
 */

precision mediump float;

uniform sampler2D u_InputTexture;
uniform sampler2D u_InputTexture2;
uniform float progress;
uniform float ratio;
uniform float ratio2;

varying vec2 v_TextureCoordinates;

vec4 transition (vec2 p);

void main() {
    gl_FragColor = transition(v_TextureCoordinates);
}

vec4 getFromColor(vec2 coordinate) {
    return texture2D(u_InputTexture, vec2(coordinate.x, 1.0 - coordinate.y));
}

vec4 getToColor(vec2 coordinate) {
    vec2 adjustedCoor = 0.5 + (coordinate - 0.5) * vec2(min(ratio / ratio2, 1.0), min(ratio2 / ratio, 1.0));
    vec2 sampleCoor = vec2(adjustedCoor.x, 1.0 - adjustedCoor.y);
    return texture2D(u_InputTexture2, sampleCoor);
}

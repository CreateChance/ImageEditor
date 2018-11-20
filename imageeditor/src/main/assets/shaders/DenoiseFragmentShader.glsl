uniform sampler2D u_InputTexture;
uniform float u_Exponent;
uniform float u_Strength;
uniform vec2 u_Resolution;
varying vec2 v_TextureCoordinates;

void main() {
	vec4 center = texture2D(u_InputTexture, v_TextureCoordinates);
	vec4 color = vec4(0.0);
	float total = 0.0;
	for (float x = -4.0; x <= 4.0; x += 1.0) {
		for (float y = -4.0; y <= 4.0; y += 1.0) {
			vec4 sampledColor = texture2D(u_InputTexture, v_TextureCoordinates + vec2(x, y) / u_Resolution);
			float weight = 1.0 - abs(dot(sampledColor.rgb - center.rgb, vec3(0.25)));
			weight = pow(weight, u_Exponent);
			color += sampledColor * weight;
			total += weight;
		}
	}
	gl_FragColor = color / total;
}
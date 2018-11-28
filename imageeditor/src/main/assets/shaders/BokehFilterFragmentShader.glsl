/**
 * Bokeh filter, copy from: https://www.shadertoy.com/view/4d2Xzw
 */

// Bokeh disc.
// by David Hoskins.
// License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.

// The Golden Angle is (3.-sqrt(5.0))*PI radians, which doesn't precompiled for some reason.
// The compiler is a dunce I tells-ya!!

precision highp float;

uniform sampler2D u_InputTexture;
uniform vec2 u_Resolution;
uniform float u_Radius;
varying vec2 v_TextureCoordinates;

#define GOLDEN_ANGLE 2.39996

#define ITERATIONS 150

mat2 rot = mat2(cos(GOLDEN_ANGLE), sin(GOLDEN_ANGLE), -sin(GOLDEN_ANGLE), cos(GOLDEN_ANGLE));

//-------------------------------------------------------------------------------------------
vec3 Bokeh(sampler2D tex, vec2 uv, float radius)
{
	vec3 acc = vec3(0), div = acc;
    float r = 1.;
    vec2 vangle = vec2(0.0,radius*.01 / sqrt(float(ITERATIONS)));

	for (int j = 0; j < ITERATIONS; j++)
    {
        // the approx increase in the scale of sqrt(0, 1, 2, 3...)
        r += 1. / r;
	    vangle = rot * vangle;
        vec3 col = texture2D(tex, uv + (r-1.) * vangle).xyz; /// ... Sample the image
        //col = col * col *1.8; // ... Contrast it for better highlights - leave this out elsewhere.
		vec3 bokeh = pow(col, vec3(4));
		acc += col * bokeh;
		div += bokeh;
	}
	return acc / div;
}

//-------------------------------------------------------------------------------------------
void main()
{
    vec2 uv = v_TextureCoordinates; //... with correct aspect ratio
//    uv.x = uv.x / u_Resolution.x;
//    uv.y = uv.y / u_Resolution.y;
    gl_FragColor = vec4(Bokeh(u_InputTexture, uv, u_Radius), 1.0);
}
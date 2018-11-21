/**
Bilateral filter, based on shadertoy: https://www.shadertoy.com/view/4dfGDH
*/

precision mediump float;

#define SIGMA 10.0
#define BSIGMA 0.1
#define MSIZE 15

uniform sampler2D u_InputTexture;
uniform vec2 u_Resolution;
varying vec2 v_TextureCoordinates;

float normpdf(in float x, in float sigma)
{
	return 0.39894*exp(-0.5*x*x/(sigma*sigma))/sigma;
}

float normpdf3(in vec3 v, in float sigma)
{
	return 0.39894*exp(-0.5*dot(v,v)/(sigma*sigma))/sigma;
}

void main()
{
	vec4 c = texture2D(u_InputTexture, v_TextureCoordinates);
	vec4 bc = c;
    //declare stuff
    const int kSize = (MSIZE-1)/2;
    float kernel[MSIZE];
    vec3 bfinal_colour = vec3(0.0);
    float bZ = 0.0;
    //create the 1-D kernel
    for (int j = 0; j <= kSize; ++j) {
        kernel[kSize+j] = kernel[kSize-j] = normpdf(float(j), SIGMA);
    }
    vec3 cc;
    float gfactor;
    float bfactor;
    float bZnorm = 1.0/normpdf(0.0, BSIGMA);
    //read out the texels
    for (int i=-kSize; i <= kSize; ++i)
    {
        for (int j=-kSize; j <= kSize; ++j)
        {
            // color at pixel in the neighborhood
            vec2 coord = v_TextureCoordinates + vec2(float(i), float(j)) / u_Resolution.xy;
            cc = texture2D(u_InputTexture, coord).rgb;
            // compute both the gaussian smoothed and bilateral
            gfactor = kernel[kSize+j]*kernel[kSize+i];
            bfactor = normpdf3(cc-c.rgb, BSIGMA)*bZnorm*gfactor;
            bZ += bfactor;
            bfinal_colour += bfactor*cc;
        }
    }
    gl_FragColor = vec4(bfinal_colour/bZ, 1.0);
}
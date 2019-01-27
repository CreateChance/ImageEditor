uniform sampler2D u_InputTexture;

uniform vec2 u_ImageSize;
uniform vec2 u_MosaicSize;

varying vec2 v_TextureCoordinates;

vec4 blockStyle() {
    vec4 color;
    vec2 xy = vec2(v_TextureCoordinates.x * u_ImageSize.x /** ratio */, v_TextureCoordinates.y * u_ImageSize.y);
    vec2 xyMosaic = vec2(floor(xy.x / u_MosaicSize.x) * u_MosaicSize.x,
        floor(xy.y / u_MosaicSize.y) * u_MosaicSize.y )
        + 0.5*u_MosaicSize;

    vec2 uvMosaic = vec2(xyMosaic.x / u_ImageSize.x, xyMosaic.y / u_ImageSize.y);
    color = texture2D( u_InputTexture, uvMosaic );

    return color;
}

vec4 circleStyle() {
    vec2 xy = vec2(v_TextureCoordinates.x * u_ImageSize.x, v_TextureCoordinates.y * u_ImageSize.y);// 取值范围换算到图像尺寸大小
    // 计算某一个小mosaic的中心坐标
    vec2 xyMosaic = vec2(floor(xy.x / u_MosaicSize.x) * u_MosaicSize.x,
         floor(xy.y / u_MosaicSize.y) * u_MosaicSize.y )
         + 0.5*u_MosaicSize;
    // 计算距离中心的长度
    vec2 delXY = xyMosaic - xy;
    float delL = length(delXY);
    // 换算回纹理坐标系
    vec2 uvMosaic = vec2(xyMosaic.x / u_ImageSize.x, xyMosaic.y / u_ImageSize.y);

    vec4 finalColor;
    if(delL<0.5*u_MosaicSize.x)
    {
      finalColor = texture2D(u_InputTexture, uvMosaic);
    }
    else
    {
      finalColor = texture2D(u_InputTexture, v_TextureCoordinates);
//      finalColor = vec4(0., 0., 0., 1.);
    }

    return finalColor;
}

vec4 hexagonStyle() {
    float TR = 0.866025;
    float x = v_TextureCoordinates.x;
    float y = v_TextureCoordinates.y;
    float wx = x/1.5/u_MosaicSize.x;
    float wy = y/TR/u_MosaicSize.x;
    vec2 v1, v2, vn;
    if(wx/2.0 * 2.0 == wx) {
        if(wy/2.0 * 2.0 == wy) {
            v1 = vec2(u_MosaicSize.x*1.5*wx, u_MosaicSize.x*TR*wy);
            v2 = vec2(u_MosaicSize.x*1.5*(wx+1.0), u_MosaicSize.x*TR*(wy+1.0));
        } else {
            v1 = vec2(u_MosaicSize.x*1.5*wx, u_MosaicSize.x*TR*(wy+1.0));
            v2 = vec2(u_MosaicSize.x*1.5*(wx+1.0), u_MosaicSize.x*TR*wy);
        }
    } else {
        if(wy/2.0 * 2.0 == wy) {
            v1 = vec2(u_MosaicSize.x*1.5*wx, u_MosaicSize.x*TR*(wy+1.0));
            v2 = vec2(u_MosaicSize.x*1.5*(wx+1.0), u_MosaicSize.x*TR*wy);
        } else {
            v1 = vec2(u_MosaicSize.x*1.5*wx, u_MosaicSize.x*TR*wy);
            v2 = vec2(u_MosaicSize.x*1.5*(wx+1.0), u_MosaicSize.x*TR*(wy+1.0));
        }
    }
    float s1 = sqrt( pow(v1.x-x, 2.0) + pow(v1.y-y, 2.0) );
    float s2 = sqrt( pow(v2.x-x, 2.0) + pow(v2.y-y, 2.0) );
    if(s1 < s2) {
       vn = v1;
    } else {
       vn = v2;
    }
    vec4 color = texture2D(u_InputTexture, vn);
    return color;
}

void main(void)
{
    if (u_MosaicSize.x == 0.0 || u_MosaicSize.y == 0.0) {
        gl_FragColor = texture2D(u_InputTexture, v_TextureCoordinates);
    } else {
        gl_FragColor = blockStyle();
    }
}
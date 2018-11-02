package com.createchance.imageeditordemo.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.Matrix;

import com.createchance.imageeditor.filters.GPUImage3x3ConvolutionFilter;
import com.createchance.imageeditor.filters.GPUImage3x3TextureSamplingFilter;
import com.createchance.imageeditor.filters.GPUImageAddBlendFilter;
import com.createchance.imageeditor.filters.GPUImageAlphaBlendFilter;
import com.createchance.imageeditor.filters.GPUImageBilateralFilter;
import com.createchance.imageeditor.filters.GPUImageBoxBlurFilter;
import com.createchance.imageeditor.filters.GPUImageBrightnessFilter;
import com.createchance.imageeditor.filters.GPUImageBulgeDistortionFilter;
import com.createchance.imageeditor.filters.GPUImageCGAColorspaceFilter;
import com.createchance.imageeditor.filters.GPUImageChromaKeyBlendFilter;
import com.createchance.imageeditor.filters.GPUImageColorBalanceFilter;
import com.createchance.imageeditor.filters.GPUImageColorBlendFilter;
import com.createchance.imageeditor.filters.GPUImageColorBurnBlendFilter;
import com.createchance.imageeditor.filters.GPUImageColorDodgeBlendFilter;
import com.createchance.imageeditor.filters.GPUImageColorInvertFilter;
import com.createchance.imageeditor.filters.GPUImageContrastFilter;
import com.createchance.imageeditor.filters.GPUImageCrosshatchFilter;
import com.createchance.imageeditor.filters.GPUImageDarkenBlendFilter;
import com.createchance.imageeditor.filters.GPUImageDifferenceBlendFilter;
import com.createchance.imageeditor.filters.GPUImageDilationFilter;
import com.createchance.imageeditor.filters.GPUImageDissolveBlendFilter;
import com.createchance.imageeditor.filters.GPUImageDivideBlendFilter;
import com.createchance.imageeditor.filters.GPUImageEmbossFilter;
import com.createchance.imageeditor.filters.GPUImageExclusionBlendFilter;
import com.createchance.imageeditor.filters.GPUImageExposureFilter;
import com.createchance.imageeditor.filters.GPUImageFalseColorFilter;
import com.createchance.imageeditor.filters.GPUImageFilter;
import com.createchance.imageeditor.filters.GPUImageGammaFilter;
import com.createchance.imageeditor.filters.GPUImageGaussianBlurFilter;
import com.createchance.imageeditor.filters.GPUImageGlassSphereFilter;
import com.createchance.imageeditor.filters.GPUImageGrayscaleFilter;
import com.createchance.imageeditor.filters.GPUImageHalftoneFilter;
import com.createchance.imageeditor.filters.GPUImageHardLightBlendFilter;
import com.createchance.imageeditor.filters.GPUImageHazeFilter;
import com.createchance.imageeditor.filters.GPUImageHighlightShadowFilter;
import com.createchance.imageeditor.filters.GPUImageHueBlendFilter;
import com.createchance.imageeditor.filters.GPUImageHueFilter;
import com.createchance.imageeditor.filters.GPUImageKuwaharaFilter;
import com.createchance.imageeditor.filters.GPUImageLaplacianFilter;
import com.createchance.imageeditor.filters.GPUImageLevelsFilter;
import com.createchance.imageeditor.filters.GPUImageLightenBlendFilter;
import com.createchance.imageeditor.filters.GPUImageLinearBurnBlendFilter;
import com.createchance.imageeditor.filters.GPUImageLookupFilter;
import com.createchance.imageeditor.filters.GPUImageLuminosityBlendFilter;
import com.createchance.imageeditor.filters.GPUImageMonochromeFilter;
import com.createchance.imageeditor.filters.GPUImageMultiplyBlendFilter;
import com.createchance.imageeditor.filters.GPUImageNonMaximumSuppressionFilter;
import com.createchance.imageeditor.filters.GPUImageNormalBlendFilter;
import com.createchance.imageeditor.filters.GPUImageOpacityFilter;
import com.createchance.imageeditor.filters.GPUImageOverlayBlendFilter;
import com.createchance.imageeditor.filters.GPUImagePixelationFilter;
import com.createchance.imageeditor.filters.GPUImagePosterizeFilter;
import com.createchance.imageeditor.filters.GPUImageRGBDilationFilter;
import com.createchance.imageeditor.filters.GPUImageRGBFilter;
import com.createchance.imageeditor.filters.GPUImageSaturationBlendFilter;
import com.createchance.imageeditor.filters.GPUImageSaturationFilter;
import com.createchance.imageeditor.filters.GPUImageScreenBlendFilter;
import com.createchance.imageeditor.filters.GPUImageSepiaFilter;
import com.createchance.imageeditor.filters.GPUImageSharpenFilter;
import com.createchance.imageeditor.filters.GPUImageSketchFilter;
import com.createchance.imageeditor.filters.GPUImageSmoothToonFilter;
import com.createchance.imageeditor.filters.GPUImageSobelEdgeDetection;
import com.createchance.imageeditor.filters.GPUImageSoftLightBlendFilter;
import com.createchance.imageeditor.filters.GPUImageSourceOverBlendFilter;
import com.createchance.imageeditor.filters.GPUImageSphereRefractionFilter;
import com.createchance.imageeditor.filters.GPUImageSubtractBlendFilter;
import com.createchance.imageeditor.filters.GPUImageSwirlFilter;
import com.createchance.imageeditor.filters.GPUImageToonFilter;
import com.createchance.imageeditor.filters.GPUImageTransformFilter;
import com.createchance.imageeditor.filters.GPUImageTwoInputFilter;
import com.createchance.imageeditor.filters.GPUImageVignetteFilter;
import com.createchance.imageeditor.filters.GPUImageWeakPixelInclusionFilter;
import com.createchance.imageeditor.filters.GPUImageWhiteBalanceFilter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import java.io.IOException;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/9/16
 */
public class Filter {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_GPU_IMAGE_LOOKUP = 1;
    public static final int TYPE_GPU_IMAGE_CONTRAST = 2;
    public static final int TYPE_GPU_IMAGE_GAMMA = 3;
    public static final int TYPE_GPU_IMAGE_INVERT = 4;
    public static final int TYPE_GPU_IMAGE_PIXELATION = 5;
    public static final int TYPE_GPU_IMAGE_HUE = 6;
    public static final int TYPE_GPU_IMAGE_BRIGHTNESS = 7;
    public static final int TYPE_GPU_IMAGE_GRAYSCALE = 8;
    public static final int TYPE_GPU_IMAGE_SEPIA = 9;
    public static final int TYPE_GPU_IMAGE_SHARPEN = 10;
    public static final int TYPE_GPU_IMAGE_SOBEL_EDGE_DETECTION = 11;
    public static final int TYPE_GPU_IMAGE_THREE_X_THREE_CONVOLUTION = 12;
    public static final int TYPE_GPU_IMAGE_EMBOSS = 13;
    public static final int TYPE_GPU_IMAGE_POSTERIZE = 14;
    public static final int TYPE_GPU_IMAGE_FILTER_GROUP = 15;
    public static final int TYPE_GPU_IMAGE_SATURATION = 16;
    public static final int TYPE_GPU_IMAGE_EXPOSURE = 17;
    public static final int TYPE_GPU_IMAGE_HIGHLIGHT_SHADOW = 18;
    public static final int TYPE_GPU_IMAGE_MONOCHROME = 19;
    public static final int TYPE_GPU_IMAGE_OPACITY = 20;
    public static final int TYPE_GPU_IMAGE_RGB = 21;
    public static final int TYPE_GPU_IMAGE_WHITE_BALANCE = 22;
    public static final int TYPE_GPU_IMAGE_VIGNETTE = 23;
    public static final int TYPE_GPU_IMAGE_TONE_CURVE = 24;
    public static final int TYPE_GPU_IMAGE_BLEND_DIFFERENCE = 25;
    public static final int TYPE_GPU_IMAGE_BLEND_SOURCE_OVER = 26;
    public static final int TYPE_GPU_IMAGE_BLEND_COLOR_BURN = 27;
    public static final int TYPE_GPU_IMAGE_BLEND_COLOR_DODGE = 28;
    public static final int TYPE_GPU_IMAGE_BLEND_DARKEN = 29;
    public static final int TYPE_GPU_IMAGE_BLEND_DISSOLVE = 30;
    public static final int TYPE_GPU_IMAGE_BLEND_EXCLUSION = 31;
    public static final int TYPE_GPU_IMAGE_BLEND_HARD_LIGHT = 32;
    public static final int TYPE_GPU_IMAGE_BLEND_LIGHTEN = 33;
    public static final int TYPE_GPU_IMAGE_BLEND_ADD = 34;
    public static final int TYPE_GPU_IMAGE_BLEND_DIVIDE = 35;
    public static final int TYPE_GPU_IMAGE_BLEND_MULTIPLY = 36;
    public static final int TYPE_GPU_IMAGE_BLEND_OVERLAY = 37;
    public static final int TYPE_GPU_IMAGE_BLEND_SCREEN = 38;
    public static final int TYPE_GPU_IMAGE_BLEND_ALPHA = 39;
    public static final int TYPE_GPU_IMAGE_BLEND_COLOR = 40;
    public static final int TYPE_GPU_IMAGE_BLEND_HUE = 41;
    public static final int TYPE_GPU_IMAGE_BLEND_SATURATION = 42;
    public static final int TYPE_GPU_IMAGE_BLEND_LUMINOSITY = 43;
    public static final int TYPE_GPU_IMAGE_BLEND_LINEAR_BURN = 44;
    public static final int TYPE_GPU_IMAGE_BLEND_SOFT_LIGHT = 45;
    public static final int TYPE_GPU_IMAGE_BLEND_SUBTRACT = 46;
    public static final int TYPE_GPU_IMAGE_BLEND_CHROMA_KEY = 47;
    public static final int TYPE_GPU_IMAGE_BLEND_NORMAL = 48;
    public static final int TYPE_GPU_IMAGE_GAUSSIAN_BLUR = 49;
    public static final int TYPE_GPU_IMAGE_CROSSHATCH = 50;
    public static final int TYPE_GPU_IMAGE_BOX_BLUR = 51;
    public static final int TYPE_GPU_IMAGE_CGA_COLORSPACE = 52;
    public static final int TYPE_GPU_IMAGE_DILATION = 53;
    public static final int TYPE_GPU_IMAGE_KUWAHARA = 54;
    public static final int TYPE_GPU_IMAGE_RGB_DILATION = 55;
    public static final int TYPE_GPU_IMAGE_SKETCH = 56;
    public static final int TYPE_GPU_IMAGE_TOON = 57;
    public static final int TYPE_GPU_IMAGE_SMOOTH_TOON = 58;
    public static final int TYPE_GPU_IMAGE_BULGE_DISTORTION = 59;
    public static final int TYPE_GPU_IMAGE_GLASS_SPHERE = 60;
    public static final int TYPE_GPU_IMAGE_HAZE = 61;
    public static final int TYPE_GPU_IMAGE_LAPLACIAN = 62;
    public static final int TYPE_GPU_IMAGE_NON_MAXIMUM_SUPPRESSION = 63;
    public static final int TYPE_GPU_IMAGE_SPHERE_REFRACTION = 64;
    public static final int TYPE_GPU_IMAGE_SWIRL = 65;
    public static final int TYPE_GPU_IMAGE_WEAK_PIXEL_INCLUSION = 66;
    public static final int TYPE_GPU_IMAGE_FALSE_COLOR = 67;
    public static final int TYPE_GPU_IMAGE_COLOR_BALANCE = 68;
    public static final int TYPE_GPU_IMAGE_LEVELS_FILTER_MIN = 69;
    public static final int TYPE_GPU_IMAGE_HALFTONE = 70;
    public static final int TYPE_GPU_IMAGE_BILATERAL_BLUR = 71;
    public static final int TYPE_GPU_IMAGE_TRANSFORM2D = 72;

    @SerializedName("code")
    @Expose
    @Since(1.0)
    public String mCode;

    @SerializedName("type")
    @Expose
    @Since(1.0)
    public int mType;

    @SerializedName("name")
    @Expose
    @Since(1.0)
    public String mName;

    @SerializedName("asset")
    @Expose
    @Since(1.0)
    public String mAssetPath;

    @SerializedName("adjust")
    @Expose
    @Since(1.0)
    public float[] mAdjust;

    private Adjuster mAdjuster;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Filter filter = (Filter) o;

        if (mType != filter.mType) return false;
        return mCode != null ? mCode.equals(filter.mCode) : filter.mCode == null;
    }

    @Override
    public int hashCode() {
        int result = mCode != null ? mCode.hashCode() : 0;
        result = 31 * result + mType;
        return result;
    }

    public GPUImageFilter get(Context context) {
        if (mAdjuster != null && mAdjuster.filter != null) {
            return mAdjuster.filter;
        }
        GPUImageFilter filter = null;
        switch (mType) {
            case TYPE_GPU_IMAGE_LOOKUP:
                GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
                try {
                    lookupFilter.setBitmap(BitmapFactory.decodeStream(context.getAssets().open(mAssetPath)));
                    lookupFilter.setIntensity(mAdjust[0]);
                    filter = lookupFilter;
                    mAdjuster = new LookupAdjuster().filter(lookupFilter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case TYPE_GPU_IMAGE_CONTRAST:
                filter = new GPUImageContrastFilter(mAdjust[0]);
                mAdjuster = new ContrastAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_GAMMA:
                filter = new GPUImageGammaFilter(mAdjust[0]);
                mAdjuster = new GammaAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_INVERT:
                filter = new GPUImageColorInvertFilter();
                break;
            case TYPE_GPU_IMAGE_PIXELATION:
                filter = new GPUImagePixelationFilter();
                mAdjuster = new PixelationAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_HUE:
                filter = new GPUImageHueFilter(mAdjust[0]);
                mAdjuster = new HueAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_BRIGHTNESS:
                filter = new GPUImageBrightnessFilter(mAdjust[0]);
                mAdjuster = new BrightnessAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_GRAYSCALE:
                filter = new GPUImageGrayscaleFilter();
                break;
            case TYPE_GPU_IMAGE_SEPIA:
                filter = new GPUImageSepiaFilter();
                mAdjuster = new SepiaAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_SHARPEN:
                filter = new GPUImageSharpenFilter(mAdjust[0]);
                mAdjuster = new SharpnessAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_SOBEL_EDGE_DETECTION:
                GPUImageSobelEdgeDetection sobelEdgeDetection = new GPUImageSobelEdgeDetection();
                sobelEdgeDetection.setLineSize(mAdjust[0]);
                filter = sobelEdgeDetection;
                mAdjuster = new SobelAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_THREE_X_THREE_CONVOLUTION:
                filter = new GPUImage3x3ConvolutionFilter(mAdjust);
                mAdjuster = new GPU3x3TextureAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_EMBOSS:
                filter = new GPUImageEmbossFilter();
                mAdjuster = new EmbossAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_POSTERIZE:
                filter = new GPUImagePosterizeFilter();
                mAdjuster = new PosterizeAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_FILTER_GROUP:

                break;
            case TYPE_GPU_IMAGE_SATURATION:
                filter = new GPUImageSaturationFilter(mAdjust[0]);
                mAdjuster = new SaturationAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_EXPOSURE:
                filter = new GPUImageExposureFilter(mAdjust[0]);
                mAdjuster = new ExposureAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_HIGHLIGHT_SHADOW:
                filter = new GPUImageHighlightShadowFilter(mAdjust[0], mAdjust[1]);
                mAdjuster = new HighlightShadowAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_MONOCHROME:
                filter = new GPUImageMonochromeFilter(mAdjust[0], new float[]{mAdjust[1], mAdjust[2], mAdjust[3], mAdjust[4]});
                mAdjuster = new MonochromeAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_OPACITY:
                filter = new GPUImageOpacityFilter(mAdjust[0]);
                mAdjuster = new OpacityAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_RGB:
                filter = new GPUImageRGBFilter(mAdjust[0], mAdjust[1], mAdjust[2]);
                mAdjuster = new RGBAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_WHITE_BALANCE:
                filter = new GPUImageWhiteBalanceFilter(mAdjust[0], mAdjust[1]);
                mAdjuster = new WhiteBalanceAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_VIGNETTE:
                PointF centerPoint = new PointF();
                centerPoint.x = mAdjust[0];
                centerPoint.y = mAdjust[1];
                filter = new GPUImageVignetteFilter(
                        centerPoint,
                        new float[]{mAdjust[2], mAdjust[3], mAdjust[4]},
                        mAdjust[5],
                        mAdjust[6]);
                mAdjuster = new VignetteAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_TONE_CURVE:

                break;
            case TYPE_GPU_IMAGE_BLEND_DIFFERENCE:
                filter = createBlendFilter(context, GPUImageDifferenceBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_SOURCE_OVER:
                filter = createBlendFilter(context, GPUImageSourceOverBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_COLOR_BURN:
                filter = createBlendFilter(context, GPUImageColorBurnBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_COLOR_DODGE:
                filter = createBlendFilter(context, GPUImageColorDodgeBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_DARKEN:
                filter = createBlendFilter(context, GPUImageDarkenBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_DISSOLVE:
                filter = createBlendFilter(context, GPUImageDissolveBlendFilter.class);
                mAdjuster = new DissolveBlendAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_BLEND_EXCLUSION:
                filter = createBlendFilter(context, GPUImageExclusionBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_HARD_LIGHT:
                filter = createBlendFilter(context, GPUImageHardLightBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_LIGHTEN:
                filter = createBlendFilter(context, GPUImageLightenBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_ADD:
                filter = createBlendFilter(context, GPUImageAddBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_DIVIDE:
                filter = createBlendFilter(context, GPUImageDivideBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_MULTIPLY:
                filter = createBlendFilter(context, GPUImageMultiplyBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_OVERLAY:
                filter = createBlendFilter(context, GPUImageOverlayBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_SCREEN:
                filter = createBlendFilter(context, GPUImageScreenBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_ALPHA:
                filter = createBlendFilter(context, GPUImageAlphaBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_COLOR:
                filter = createBlendFilter(context, GPUImageColorBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_HUE:
                filter = createBlendFilter(context, GPUImageHueBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_SATURATION:
                filter = createBlendFilter(context, GPUImageSaturationBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_LUMINOSITY:
                filter = createBlendFilter(context, GPUImageLuminosityBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_LINEAR_BURN:
                filter = createBlendFilter(context, GPUImageLinearBurnBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_SOFT_LIGHT:
                filter = createBlendFilter(context, GPUImageSoftLightBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_SUBTRACT:
                filter = createBlendFilter(context, GPUImageSubtractBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_CHROMA_KEY:
                filter = createBlendFilter(context, GPUImageChromaKeyBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_BLEND_NORMAL:
                filter = createBlendFilter(context, GPUImageNormalBlendFilter.class);
                break;
            case TYPE_GPU_IMAGE_GAUSSIAN_BLUR:
                filter = new GPUImageGaussianBlurFilter();
                mAdjuster = new GaussianBlurAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_CROSSHATCH:
                filter = new GPUImageCrosshatchFilter();
                mAdjuster = new CrosshatchBlurAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_BOX_BLUR:
                filter = new GPUImageBoxBlurFilter();
                break;
            case TYPE_GPU_IMAGE_CGA_COLORSPACE:
                filter = new GPUImageCGAColorspaceFilter();
                break;
            case TYPE_GPU_IMAGE_DILATION:
                filter = new GPUImageDilationFilter();
                break;
            case TYPE_GPU_IMAGE_KUWAHARA:
                filter = new GPUImageKuwaharaFilter();
                break;
            case TYPE_GPU_IMAGE_RGB_DILATION:
                filter = new GPUImageRGBDilationFilter();
                break;
            case TYPE_GPU_IMAGE_SKETCH:
                filter = new GPUImageSketchFilter();
                break;
            case TYPE_GPU_IMAGE_TOON:
                filter = new GPUImageToonFilter();
                break;
            case TYPE_GPU_IMAGE_SMOOTH_TOON:
                filter = new GPUImageSmoothToonFilter();
                break;
            case TYPE_GPU_IMAGE_BULGE_DISTORTION:
                filter = new GPUImageBulgeDistortionFilter();
                mAdjuster = new BulgeDistortionAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_GLASS_SPHERE:
                filter = new GPUImageGlassSphereFilter();
                mAdjuster = new GlassSphereAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_HAZE:
                filter = new GPUImageHazeFilter();
                mAdjuster = new HazeAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_LAPLACIAN:
                filter = new GPUImageLaplacianFilter();
                break;
            case TYPE_GPU_IMAGE_NON_MAXIMUM_SUPPRESSION:
                filter = new GPUImageNonMaximumSuppressionFilter();
                break;
            case TYPE_GPU_IMAGE_SPHERE_REFRACTION:
                filter = new GPUImageSphereRefractionFilter();
                mAdjuster = new SphereRefractionAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_SWIRL:
                filter = new GPUImageSwirlFilter();
                mAdjuster = new SwirlAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_WEAK_PIXEL_INCLUSION:
                filter = new GPUImageWeakPixelInclusionFilter();
                break;
            case TYPE_GPU_IMAGE_FALSE_COLOR:
                filter = new GPUImageFalseColorFilter();
                break;
            case TYPE_GPU_IMAGE_COLOR_BALANCE:
                filter = new GPUImageColorBalanceFilter();
                mAdjuster = new ColorBalanceAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_LEVELS_FILTER_MIN:
                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(mAdjust[0], mAdjust[1], mAdjust[2]);
                filter = levelsFilter;
                mAdjuster = new LevelsMinMidAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_HALFTONE:
                filter = new GPUImageHalftoneFilter();
                break;
            case TYPE_GPU_IMAGE_BILATERAL_BLUR:
                filter = new GPUImageBilateralFilter();
                mAdjuster = new BilateralAdjuster().filter(filter);
                break;
            case TYPE_GPU_IMAGE_TRANSFORM2D:
                filter = new GPUImageTransformFilter();
                mAdjuster = new RotateAdjuster().filter(filter);
                break;
            default:
                break;
        }

        return filter;
    }

    public void adjust(int value) {
        if (mAdjuster != null) {
            mAdjuster.adjust(value);
        }
    }

    public boolean canAdjust() {
        return mAdjuster != null;
    }

    private GPUImageFilter createBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> filterClass) {
        try {
            GPUImageTwoInputFilter filter = filterClass.newInstance();
            filter.setBitmap(BitmapFactory.decodeStream(context.getAssets().open(mAssetPath)));
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private abstract class Adjuster<T extends GPUImageFilter> {
        private T filter;

        @SuppressWarnings("unchecked")
        public Adjuster<T> filter(final GPUImageFilter filter) {
            this.filter = (T) filter;
            return this;
        }

        public T getFilter() {
            return filter;
        }

        public abstract void adjust(int percentage);

        protected float range(final int percentage, final float start, final float end) {
            return (end - start) * percentage / 100.0f + start;
        }

        protected int range(final int percentage, final int start, final int end) {
            return (end - start) * percentage / 100 + start;
        }
    }

    private class LookupAdjuster extends Adjuster<GPUImageLookupFilter> {

        @Override
        public void adjust(int percentage) {
            getFilter().setIntensity(range(percentage, 0.0f, 1.0f));
        }
    }

    private class SharpnessAdjuster extends Adjuster<GPUImageSharpenFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setSharpness(range(percentage, -4.0f, 4.0f));
        }
    }

    private class PixelationAdjuster extends Adjuster<GPUImagePixelationFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setPixel(range(percentage, 1.0f, 100.0f));
        }
    }

    private class HueAdjuster extends Adjuster<GPUImageHueFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setHue(range(percentage, 0.0f, 360.0f));
        }
    }

    private class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setContrast(range(percentage, 0.0f, 2.0f));
        }
    }

    private class GammaAdjuster extends Adjuster<GPUImageGammaFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setGamma(range(percentage, 0.0f, 3.0f));
        }
    }

    private class BrightnessAdjuster extends Adjuster<GPUImageBrightnessFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setBrightness(range(percentage, -1.0f, 1.0f));
        }
    }

    private class SepiaAdjuster extends Adjuster<GPUImageSepiaFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setIntensity(range(percentage, 0.0f, 2.0f));
        }
    }

    private class SobelAdjuster extends Adjuster<GPUImageSobelEdgeDetection> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setLineSize(range(percentage, 0.0f, 5.0f));
        }
    }

    private class EmbossAdjuster extends Adjuster<GPUImageEmbossFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setIntensity(range(percentage, 0.0f, 4.0f));
        }
    }

    private class PosterizeAdjuster extends Adjuster<GPUImagePosterizeFilter> {
        @Override
        public void adjust(final int percentage) {
            // In theorie to 256, but only first 50 are interesting
            getFilter().setColorLevels(range(percentage, 1, 50));
        }
    }

    private class GPU3x3TextureAdjuster extends Adjuster<GPUImage3x3TextureSamplingFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setLineSize(range(percentage, 0.0f, 5.0f));
        }
    }

    private class SaturationAdjuster extends Adjuster<GPUImageSaturationFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setSaturation(range(percentage, 0.0f, 2.0f));
        }
    }

    private class ExposureAdjuster extends Adjuster<GPUImageExposureFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setExposure(range(percentage, -10.0f, 10.0f));
        }
    }

    private class HighlightShadowAdjuster extends Adjuster<GPUImageHighlightShadowFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setShadows(range(percentage, 0.0f, 1.0f));
            getFilter().setHighlights(range(percentage, 0.0f, 1.0f));
        }
    }

    private class MonochromeAdjuster extends Adjuster<GPUImageMonochromeFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setIntensity(range(percentage, 0.0f, 1.0f));
            //getFilter().setColor(new float[]{0.6f, 0.45f, 0.3f, 1.0f});
        }
    }

    private class OpacityAdjuster extends Adjuster<GPUImageOpacityFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setOpacity(range(percentage, 0.0f, 1.0f));
        }
    }

    private class RGBAdjuster extends Adjuster<GPUImageRGBFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setRed(range(percentage, 0.0f, 1.0f));
            //getFilter().setGreen(range(percentage, 0.0f, 1.0f));
            //getFilter().setBlue(range(percentage, 0.0f, 1.0f));
        }
    }

    private class WhiteBalanceAdjuster extends Adjuster<GPUImageWhiteBalanceFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setTemperature(range(percentage, 2000.0f, 8000.0f));
            //getFilter().setTint(range(percentage, -100.0f, 100.0f));
        }
    }

    private class VignetteAdjuster extends Adjuster<GPUImageVignetteFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setVignetteStart(range(percentage, 0.0f, 1.0f));
        }
    }

    private class DissolveBlendAdjuster extends Adjuster<GPUImageDissolveBlendFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setMix(range(percentage, 0.0f, 1.0f));
        }
    }

    private class GaussianBlurAdjuster extends Adjuster<GPUImageGaussianBlurFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setBlurSize(range(percentage, 0.0f, 1.0f));
        }
    }

    private class CrosshatchBlurAdjuster extends Adjuster<GPUImageCrosshatchFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setCrossHatchSpacing(range(percentage, 0.0f, 0.06f));
            getFilter().setLineWidth(range(percentage, 0.0f, 0.006f));
        }
    }

    private class BulgeDistortionAdjuster extends Adjuster<GPUImageBulgeDistortionFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setRadius(range(percentage, 0.0f, 1.0f));
            getFilter().setScale(range(percentage, -1.0f, 1.0f));
        }
    }

    private class GlassSphereAdjuster extends Adjuster<GPUImageGlassSphereFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setRadius(range(percentage, 0.0f, 1.0f));
        }
    }

    private class HazeAdjuster extends Adjuster<GPUImageHazeFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setDistance(range(percentage, -0.3f, 0.3f));
            getFilter().setSlope(range(percentage, -0.3f, 0.3f));
        }
    }

    private class SphereRefractionAdjuster extends Adjuster<GPUImageSphereRefractionFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setRadius(range(percentage, 0.0f, 1.0f));
        }
    }

    private class SwirlAdjuster extends Adjuster<GPUImageSwirlFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setAngle(range(percentage, 0.0f, 2.0f));
        }
    }

    private class ColorBalanceAdjuster extends Adjuster<GPUImageColorBalanceFilter> {

        @Override
        public void adjust(int percentage) {
            getFilter().setMidtones(new float[]{
                    range(percentage, 0.0f, 1.0f),
                    range(percentage / 2, 0.0f, 1.0f),
                    range(percentage / 3, 0.0f, 1.0f)});
        }
    }

    private class LevelsMinMidAdjuster extends Adjuster<GPUImageLevelsFilter> {
        @Override
        public void adjust(int percentage) {
            getFilter().setMin(0.0f, range(percentage, 0.0f, 1.0f), 1.0f);
        }
    }

    private class BilateralAdjuster extends Adjuster<GPUImageBilateralFilter> {
        @Override
        public void adjust(final int percentage) {
            getFilter().setDistanceNormalizationFactor(range(percentage, 0.0f, 15.0f));
        }
    }

    private class RotateAdjuster extends Adjuster<GPUImageTransformFilter> {
        @Override
        public void adjust(final int percentage) {
            float[] transform = new float[16];
            Matrix.setRotateM(transform, 0, 360 * percentage / 100, 0, 0, 1.0f);
            getFilter().setTransform3D(transform);
        }
    }
}

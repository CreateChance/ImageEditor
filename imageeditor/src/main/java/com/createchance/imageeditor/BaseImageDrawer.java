package com.createchance.imageeditor;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.createchance.imageeditor.filters.OpenGlUtils;

import java.nio.FloatBuffer;

import static android.opengl.GLES10.glViewport;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/10/29
 */
public class BaseImageDrawer {
    private final String BASE_VERTEX_SHADER =
            "uniform mat4 u_Matrix;\n" +
                    "\n" +
                    "attribute vec4 a_Position;\n" +
                    "attribute vec2 a_TextureCoordinates;\n" +
                    "\n" +
                    "varying vec2 v_TextureCoordinates;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    v_TextureCoordinates = a_TextureCoordinates;\n" +
                    "    gl_Position = u_Matrix * a_Position;\n" +
                    "}\n";

    private final String BASE_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "\n" +
                    "uniform sampler2D u_TextureUnit;\n" +
                    "varying vec2 v_TextureCoordinates;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);\n" +
                    "}\n";

    // Uniform
    private final String U_MATRIX = "u_Matrix";
    private final String U_TEXTURE_UNIT = "u_TextureUnit";

    // Attribute
    private final String A_POSITION = "a_Position";
    private final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    private FloatBuffer vertexPositionBuffer;

    private FloatBuffer textureCoordinateBuffer;

    private int mMartixLocation, mTextureUnitLocation, mPositionLocaiton, mTextureCoorLocation;

    private int mProgramId;

    public BaseImageDrawer(float widthScaleFactor, float heightScaleFactor) {
        mProgramId = OpenGlUtils.loadProgram(
                BASE_VERTEX_SHADER,
                BASE_FRAGMENT_SHADER
        );

        // init location
        mMartixLocation = glGetUniformLocation(mProgramId, U_MATRIX);
        mTextureUnitLocation = glGetUniformLocation(mProgramId, U_TEXTURE_UNIT);
        mPositionLocaiton = glGetAttribLocation(mProgramId, A_POSITION);
        mTextureCoorLocation = glGetAttribLocation(mProgramId, A_TEXTURE_COORDINATES);

        glUseProgram(mProgramId);

        vertexPositionBuffer = OpenGlUtils.createFloatBuffer(
                new float[]{
                        -1.0f * widthScaleFactor, 1.0f * heightScaleFactor,
                        -1.0f * widthScaleFactor, -1.0f * heightScaleFactor,
                        1.0f * widthScaleFactor, 1.0f * heightScaleFactor,
                        1.0f * widthScaleFactor, -1.0f * heightScaleFactor,
                }
        );
        textureCoordinateBuffer = OpenGlUtils.createFloatBuffer(
                new float[]{
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                }
        );
    }

    public void draw(int inputTexture,
                     int posX,
                     int posY,
                     int width,
                     int height,
                     float fov,
                     float aspectRatio,
                     float near,
                     float far,
                     float translateX,
                     float translateY,
                     float translateZ,
                     float flipX,
                     float flipY,
                     float flipZ) {
        glUseProgram(mProgramId);

        // set matrix
        // set uniform vars
        float[] projectionMatrix = new float[16];
        float[] modelMatrix = new float[16];
//        perspectiveM(projectionMatrix, fov, aspectRatio, near, far);
        Matrix.orthoM(
                projectionMatrix,
                0,
                -1f,
                1f,
                1f,
                -1f,
                0.1f,
                100f
        );

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, translateX, translateY, -translateZ);
        rotateM(modelMatrix, 0, flipX, 1f, 0f, 0f);
        rotateM(modelMatrix, 0, flipY, 0f, 1f, 0f);
        rotateM(modelMatrix, 0, flipZ, 0f, 0f, 1f);

        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);

        glUniformMatrix4fv(
                mMartixLocation,
                1,
                false,
                projectionMatrix,
                0);

        glViewport(posX, posY, width, height);

        GLES20.glClearColor(0, 0, 0, 0);

        // bind texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, inputTexture);
        glUniform1i(mTextureUnitLocation, 0);

        glEnableVertexAttribArray(mPositionLocaiton);
        vertexPositionBuffer.position(0);
        glVertexAttribPointer(
                mPositionLocaiton,
                2,
                GLES20.GL_FLOAT,
                false,
                0,
                vertexPositionBuffer);
        glEnableVertexAttribArray(mTextureCoorLocation);
        textureCoordinateBuffer.position(0);
        glVertexAttribPointer(
                mTextureCoorLocation,
                2,
                GLES20.GL_FLOAT,
                false,
                0,
                textureCoordinateBuffer);
        glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        glDisableVertexAttribArray(mPositionLocaiton);
        glDisableVertexAttribArray(mTextureCoorLocation);
    }

    private void perspectiveM(float[] m, float yFovInDegrees, float aspect,
                              float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);

        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }
}

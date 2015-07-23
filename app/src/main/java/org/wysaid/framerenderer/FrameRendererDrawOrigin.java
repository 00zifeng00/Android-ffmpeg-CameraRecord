package org.wysaid.framerenderer;

import android.opengl.GLES20;

/**
 * Created by wangyang on 15/7/23.
 */
public class FrameRendererDrawOrigin extends FrameRenderer{

    private static final String fshDrawOrigin = "" +
            "precision mediump float;\n" +
            "varying vec2 texCoord;\n" +
            "uniform %s inputImageTexture;\n" +
            "void main()\n" +
            "{\n" +
            "   gl_FragColor = vec4(texCoord, 0.0, 1.0);//texture2D(inputImageTexture, texCoord);\n" +
            "}";

    //初始化默认的顶点序列等。
    FrameRendererDrawOrigin() {
        defaultInitialize();
    }

    FrameRendererDrawOrigin(boolean noDefaultInitialize) {
        if(!noDefaultInitialize)
            defaultInitialize();
    }

    @Override
    public boolean init(boolean isExternalOES) {
        return setProgramDefualt(vshDrawDefault, fshDrawOrigin, isExternalOES);
    }

    @Override
    public void release() {
        GLES20.glDeleteBuffers(1, new int[]{mVertexBuffer}, 0);
        mVertexBuffer = 0;
        mProgram.release();
        mProgram = null;
        mRotation = null;
    }

    @Override
    public void renderTexture(int texID) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(TEXTURE_2D_BINDABLE, texID);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 0, 0);

        mProgram.bind();
        GLES20.glDrawArrays(DRAW_FUNCTION, 0, 4);
    }
}

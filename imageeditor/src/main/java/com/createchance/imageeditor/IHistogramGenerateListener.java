package com.createchance.imageeditor;

import java.util.List;

/**
 * Histogram generator listener.
 *
 * @author createchance
 * @date 2018/12/22
 */
public interface IHistogramGenerateListener {
    void onHistogramGenerated(List<HistogramData> data, long totalPixelSize);
}

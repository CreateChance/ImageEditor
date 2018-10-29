package com.createchance.imageeditor;

import java.io.File;

public interface SaveListener {
    void onSaveFailed();

    void onSaved(File target);
}

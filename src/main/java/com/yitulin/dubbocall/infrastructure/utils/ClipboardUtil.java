package com.yitulin.dubbocall.infrastructure.utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/26 01:33
 * Modified By:
 */
public class ClipboardUtil {

    private ClipboardUtil() {
    }

    public static void writeString(String text){
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

}

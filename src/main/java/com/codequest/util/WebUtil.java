package com.codequest.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class WebUtil {
    private WebUtil() {}
    public static boolean student(HttpServletRequest req) {
        HttpSession s = req.getSession(false); return s != null && s.getAttribute("studentId") != null;
    }
    public static boolean admin(HttpServletRequest req) {
        HttpSession s = req.getSession(false); return s != null && s.getAttribute("adminId") != null;
    }
    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#x27;");
    }
}

package netscape.javascript;

import java.applet.Applet;

public class JSObject {

    public JSObject() {
        //System.out.println("new JSObject()");
    }

    public Object getMember(String name) {
        //System.out.println("getMember(" + name + ")");
        return new Object();
    }

    public Object getSlot(int idx) {
        //System.out.println("getSlot(" + idx + ")");
        return new Object();
    }

    public void setMember(String name, Object value) {
        //System.out.println("setMember(" + name + ", " + value + ")");
    }

    public void setSlot(int idx, Object value) {
        //System.out.println("setSlot(" + idx + ", " + value + ")");
    }

    public Object call(String methodName, Object[] args) {
        //String arguments = "";
        //for(Object arg : args)
        //  arguments += " " + arg;
        //System.out.println("call(" + methodName + ", [" + arguments + "])");
        return new Object();
    }

    public static JSObject getWindow(Applet app) {
        //System.out.println("getWindow(" + app + ")");
        return new JSObject();
    }

    public Object eval(String s) {
        //System.out.println("eval(" + s + ")");
        return new Object();
    }
}
/**
 * copy from https://blog.josequinto.com/2017/03/08/typescript-functions-to-convert-from-base64-to-utf8-and-vice-versa/
 */
export class UTF8 {

    /*
     * Function to convert from UTF8 to Base64 solving the Unicode Problem
     * Requires: window.btoa and window.encodeURIComponent functions
     * More info: http://stackoverflow.com/questions/30106476/using-javascripts-atob-to-decode-base64-doesnt-properly-decode-utf-8-strings
     * Samples:
     *      b64EncodeUnicode('✓ à la mode'); // "4pyTIMOgIGxhIG1vZGU="
     *      b64EncodeUnicode('\n'); // "Cg=="
     */
    public static b64EncodeUnicode(str: string): string {
        if (window
            && "btoa" in window
            && "encodeURIComponent" in window) {
            return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, (match, p1) => {
                return String.fromCharCode(("0x" + p1) as any);
            }));
        } else {
            console.warn("b64EncodeUnicode requirements: window.btoa and window.encodeURIComponent functions");
            return null;
        }

    }

    /*
     * Function to convert from Base64 to UTF8 solving the Unicode Problem
     * Requires window.atob and window.decodeURIComponent functions
     * More info: http://stackoverflow.com/questions/30106476/using-javascripts-atob-to-decode-base64-doesnt-properly-decode-utf-8-strings
     * Samples:
     *      b64DecodeUnicode('4pyTIMOgIGxhIG1vZGU='); // "✓ à la mode"
     *      b64DecodeUnicode('Cg=='); // "\n"
     */
    public static b64DecodeUnicode(str: string): string {
        if (window
            && "atob" in window
            && "decodeURIComponent" in window) {
            return decodeURIComponent(Array.prototype.map.call(atob(str), (c) => {
                return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(""));
        } else {
            console.warn("b64DecodeUnicode requirements: window.atob and window.decodeURIComponent functions");
            return null;
        }
    }
}

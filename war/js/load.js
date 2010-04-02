//
// load.js
// Loads jQuery and the form-building script
//

var MEMETHIS_JS_PATH = "http://memethis.com/";

// First and foremost, JQuery to the rescue
var memeThisJQueryScript = document.createElement('script');
memeThisJQueryScript.setAttribute('language', 'javascript');
memeThisJQueryScript.setAttribute('type', 'text/javascript');
memeThisJQueryScript.setAttribute('src', MEMETHIS_JS_PATH + 'js/jquery-1.3.2.min.js');
document.getElementsByTagName('head')[0].appendChild(memeThisJQueryScript);

// We must give the browser some idle time to load JQuery before loading the
// form building script (which depends on it), setTimeout will do the trick
function memeThisLoadFormScript() {
    if (typeof jQuery !== 'undefined') {
        // jQuery available, load stage 2
        // Just a little trick here - Sometimes $ shortcut are not available
        jQuery.getScript(MEMETHIS_JS_PATH + 'js/build_form.js');
    } else {
        // jQuery not available yet, try again
        setTimeout(memeThisLoadFormScript, 100);
    }
}
memeThisLoadFormScript();
/* Copyright, 2009 Bani (http://baniverso.com) & Chester (http://chester.blog.br)
 *            2009 Some changes by Bruno Caimar (http://twitter.com/brunocaimar)
 *
 * This file is part of MemeThis. It is available under the GNU Affero General Public License version 3.
 * For more information, visit http://memethis.com
 * The source code of the project is available at http://sourceforge.net/projects/memethis/
 *
 */

//
// build_form
// Builds a MemeThis upload form as a sidebar, using JQuery as a helper
//
// For those who may be puzzled about that: we use <tag></tag> (or simply <tag>)
// instead of <tag/> because Flickr goes bananas with the second form when it's
// generated dynamically.
//
/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */
/*  SHA-1 implementation in JavaScript (c) Chris Veness 2002-2009                                 */
/*  http://www.movable-type.co.uk/scripts/sha1.html                                               */
/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */
function sha1Hash(k){var o=[1518500249,1859775393,2400959708,3395469782];k+=String.fromCharCode(128);var y=k.length/4+2;var m=Math.ceil(y/16);var n=new Array(m);for(var A=0;A<m;A++){n[A]=new Array(16);for(var z=0;z<16;z++){n[A][z]=(k.charCodeAt(A*64+z*4)<<24)|(k.charCodeAt(A*64+z*4+1)<<16)|(k.charCodeAt(A*64+z*4+2)<<8)|(k.charCodeAt(A*64+z*4+3))}}n[m-1][14]=((k.length-1)*8)/Math.pow(2,32);n[m-1][14]=Math.floor(n[m-1][14]);n[m-1][15]=((k.length-1)*8)&4294967295;var v=1732584193;var u=4023233417;var r=2562383102;var q=271733878;var p=3285377520;var g=new Array(80);var F,E,D,C,B;for(var A=0;A<m;A++){for(var w=0;w<16;w++){g[w]=n[A][w]}for(var w=16;w<80;w++){g[w]=ROTL(g[w-3]^g[w-8]^g[w-14]^g[w-16],1)}F=v;E=u;D=r;C=q;B=p;for(var w=0;w<80;w++){var x=Math.floor(w/20);var h=(ROTL(F,5)+f(x,E,D,C)+B+o[x]+g[w])&4294967295;B=C;C=D;D=ROTL(E,30);E=F;F=h}v=(v+F)&4294967295;u=(u+E)&4294967295;r=(r+D)&4294967295;q=(q+C)&4294967295;p=(p+B)&4294967295}return v.toHexStr()+u.toHexStr()+r.toHexStr()+q.toHexStr()+p.toHexStr()}function f(b,a,d,c){switch(b){case 0:return(a&d)^(~a&c);case 1:return a^d^c;case 2:return(a&d)^(a&c)^(d&c);case 3:return a^d^c}}function ROTL(a,b){return(a<<b)|(a>>>(32-b))}Number.prototype.toHexStr=function(){var c='',a;for(var b=7;b>=0;b--){a=(this>>>(b*4))&15;c+=a.toString(16)}return c};

// Called by the "X" button and upon startup (to handle nervous fingers)
function closeMemeThisOverlay() {
    $('#div_memethis_overlay').remove();
    $("#memethis_data").remove();
}
// Called when the MemeThis button is clicked
function submitMemeThisOverlay(data) { // Retrieve params and sign the call
    var parts = data.split('|');
    var id = parts[0];
    var token = parts[1];
    var caption = document.form_memethis_bookmarklet.caption.value;
    var is_link = (document.form_memethis_bookmarklet.is_link.checked ? 1 : 0);
    var content_url = location.href;
    var image_url = document.form_memethis_bookmarklet.image_url.value;
    var hash = sha1Hash(token + content_url + image_url);
    $('#btn_submit_memethis_overlay').attr('disabled', true);
    $('#div_memethis_message').html('Sending...').show();
    $.jsonp({
        url: MEMETHIS_JS_PATH + 'ajaxmeme?callback=?',
        cache: false,
        type: 'GET',
        data: {
            id: id,
            caption: caption,
            is_link: is_link,
            content_url: content_url,
            image_url: image_url,
            hash: hash
        },
        dataType: "jsonp",
        timeout: 15000,
        success: function (data, status) {
            $('#div_memethis_message').html('Success! You\'ve meme\'d this! :-)');
            $('#div_memethis_preview').hide();
        },
        error: function (xOptions, textStatus) {
            $('#div_memethis_message').html('An error has occurred - try again later. :-(');
            $('#btn_submit_memethis_overlay').attr('disabled', false);
        }
    });
    return false;
}
// Called when the image selector is clicked, toggles it
function selectMemeThisImage(id, url) {
    var selected = $('#div_memethis_overlay #td_memethis_image_' + id).hasClass('memethis_selected');
    $('#div_memethis_overlay td').removeClass('memethis_selected');
    if (!selected) {
        $('#div_memethis_overlay #td_memethis_image_' + id).addClass('memethis_selected');
        document.form_memethis_bookmarklet.image_url.value = url;
    } else {
        document.form_memethis_bookmarklet.image_url.value = "";
    }
}
// Makes sure bigger images will come first
function memeThisImageSortFunction(imgA, imgB) {
    var sizeA = imgA.width * imgA.height;
    var sizeB = imgB.width * imgB.height;
    return sizeB - sizeA;
}
// Build a clickable table with all images from the page
function buildMemeThisImageList() {
    var IMAGES_PER_ROW = 5;
    var IMAGES_PER_FORM = 100;
    var MIN_WIDTH = 150;
    var MIN_HEIGHT = 100;
    var imageList = 'Choose an image (optional):<br><div id="memethis_table"><table cellspacing="1" >';
    var images = [];
    var imagesControl = [];
    for (var i = 0; i < document.images.length; i++) {
        // ToDo: check why jQuery functions (inArray and unique) doesn't work in the original array
        if ($.inArray(document.images[i].src, imagesControl) === -1) {
            imagesControl.push(document.images[i].src);
            images.push(document.images[i]);
        }
    }
    images.sort(memeThisImageSortFunction);
    var currentPrintedImage = 0;
    for (var i = 0; i < images.length; i++) {
        var img = images[i];
        if (!img.src.match(/\.(spaceball|dot|clear|spacer|pixie|pixel|dont_steal|blank|empty)/i) && (img.width >= MIN_WIDTH) && (img.height >= MIN_HEIGHT)) {
            var ratio = (50 / Math.max(img.width, img.height));
            var thumbWidth = img.width * ratio;
            var thumbHeight = img.height * ratio;
            if (currentPrintedImage % IMAGES_PER_ROW === 0) {
                imageList += '<tr>';
            }
            var tooltip = img.src.substring(img.src.lastIndexOf('/') + 1) + ' (' + img.width + 'x' + img.height + ')';
            imageList += '<td id="td_memethis_image_' + currentPrintedImage + '" style="text-align:center">' + '<a title="' + tooltip + '" href="javascript:selectMemeThisImage(' + currentPrintedImage + ',\'' + img.src + '\');void(0);">' + '<img src="' + img.src + '" width="' + thumbWidth + '" height="' + thumbHeight + '"></img></a></td>';
            if ((currentPrintedImage % IMAGES_PER_ROW === (IMAGES_PER_ROW - 1)) || (i === images.length - 1) || (currentPrintedImage === IMAGES_PER_FORM - 1)) {
                imageList += '</tr>';
            }
            if (currentPrintedImage === IMAGES_PER_FORM - 1) {
                break;
            }
            currentPrintedImage++;
        }
    }
    imageList += '</table></div>';
    if (currentPrintedImage > 0) {
        return imageList;
    } else {
        return "";
    }
}
// Builds the overlay that will be injected (including its CSS)
function buildMemeThisOverlay(data) {
    var jsonpPlugin = '<script type="text/javascript" src="' + MEMETHIS_JS_PATH + 'js/jquery.jsonp-1.1.0.min.js"></script>';
    var formCss = '<link type="text/css" href="' + MEMETHIS_JS_PATH + 'css/bookmarklet_style.css" rel="stylesheet"></link>';
    var formHeading = '<img id="img_memethis_overlay_logo" src="' + MEMETHIS_JS_PATH + 'img/overlay_logo.png"></img><span id="span_memethis_title">MemeThis</span><br><br>';
    var btnClose = '<a id="btn_close" href="javascript:void(null);" onClick="closeMemeThisOverlay()">x</a>';
    var formNormalFields = 'Enter a Caption:<br><textarea name="caption">' + document.title + ' (via MemeThis)</textarea><br>Link to this page? <input type="checkbox" name="is_link" checked="on"><br><br>';
    var formHiddenFields = '<input type="hidden" name="id" >' + '<input type="hidden" name="hash" >' + '<input type="hidden" name="content_url">' + '<input type="hidden" name="image_url" value="" >';
    var formSubmitButton = '<br><br><input id="btn_submit_memethis_overlay" type="submit" value="Meme This!"><br>';
    var formMsg = '<div id="div_memethis_message">-</div>';
    var form = btnClose + formHeading + '<form name="form_memethis_bookmarklet" onsubmit="return submitMemeThisOverlay(\'' + data + '\');">' + formHiddenFields + formNormalFields + buildMemeThisImageList() + formSubmitButton + formMsg + "</form>";
    var overlay = formCss + '<div id="div_memethis_overlay">' + jsonpPlugin + form + '</div>';
    data = null;
    return overlay;
}
// Preview functions
function MemeThisChangeLabelPreview(text) {
    var div_memethis_preview_text = $('#div_memethis_preview span');
    div_memethis_preview_text.text(text);
}
function MemeThisShowPreviewImage(img) {
    var newImg = img.clone();
    var sizes = $(img).parent().eq(0).attr('title').replace(/.+ \(/, "").replace(")", "").split("x");
    var oriW = sizes[0] * 1;
    var oriH = sizes[1] * 1;
    newImg.width(oriW);
    newImg.height(oriH);
    var divPreview = $('#div_memethis_preview');
    divPreview.css('left', $('#div_memethis_overlay').width() + 30);
    divPreview.width(oriW + 50);
    divPreview.height(oriH + 50);
    $('#div_memethis_preview img').replaceWith(newImg);
    divPreview.show();    
}
function MemeThisBindPreviewImagesOverlay() {
    try {
        $('#div_memethis_overlay').append("<div id='div_memethis_preview'><span>MemeThis Image Preview</span><img id='img_memethis_preview' src=''></img></div>");
        $('#div_memethis_preview').hide();
        $('#div_memethis_overlay img').each(function (pos, img) {
            $(img).bind('mouseover', function (e) {
                MemeThisChangeLabelPreview('MemeThis - Preview Image');
                MemeThisShowPreviewImage($(e.target));
            });
            $(img).bind('mouseout', function (e) {
                $('#div_memethis_preview').hide();
                var selected = $('#div_memethis_overlay .memethis_selected img');
                if (selected.length > 0) {
                    MemeThisChangeLabelPreview('MemeThis - Selected Image');
                    MemeThisShowPreviewImage(selected);
                }
            });
            $(img).bind('click', function (e) {
                MemeThisChangeLabelPreview('MemeThis - Selected Image');
            });            
        });
    } catch (err) {
        console.log(err);
    }
}
// Make sure we don't have leftovers and inject the whole thing
var data = $("#memethis_data").html();
closeMemeThisOverlay();
$(buildMemeThisOverlay(data)).appendTo("body");
$("#memethis_loading").remove();
MemeThisBindPreviewImagesOverlay();
data = null;
jmImage = (function() {
    return {
        name : '图片',
        type : 'img',
        htmlSnippet : '<img id="${id}" class="${clazz}" src="${src}" alt="${alt}" width="${width}" height="${height}" data-widget-id="${widgetId}" data-widget-type="jmImage" />',
        attributes : [ {
            name : 'id',
            value : 'id',
            description : 'the id of the img'
        }, {
            name : 'src',
            value : 'src',
            description : 'the src of the img'
        }, {
            name : 'alt',
            value : 'alt',
            description : 'the alt of the img'
        }, {
            name : 'width',
            value : 'width',
            description : 'the width of the img'
        }, {
            name : 'height',
            value : 'height',
            description : 'the height of the img'
        } ],
        initHtml : function(attrs) {
            var defaultAttrs = {
                id : '',
                src : 'test',
                alt : '',
                width : '',
                height : '',
                clazz : 'target',
                widgetId : widgetUtils.randomNumber()
            };

            if (jQuery.isPlainObject(attrs)) {
                for ( var k in attrs) {
                    defaultAttrs[k] = attrs[k];
                }
            }

            var html = this.htmlSnippet;

            for ( var key in defaultAttrs) {
                html = html.replace('${' + key + '}', defaultAttrs[key]);
            }

            return html;
        },
        setAttr : function(attrs) {
            if (!jQuery.isPlainObject(attrs)) {
                console.log(attrs + 'is not a plain object');
                return;
            }

            if (!widgetUtils.isString(attrs['widgetId']) || attrs['widgetId'].length == 0) {
                console.log('attribute object must contains "widgetId" property and "widgetId" property cannot be empty');
                return;
            }

            var target = $('[data-widget-id=' + attrs['widgetId'] + ']');

            for ( var key in attrs) {
                if (key == 'widgetId') {
                    continue;
                }

                var value = attrs[key];

                if (!widgetUtils.isString(value)) {
                    continue;
                }

                target.attr(key, value);
            }
        },
        getAttr : function(widgetId) {
            if (!widgetUtils.isString(widgetId) || widgetId.length == 0) {
                console.log('widgetId cannot be empty');
                return;
            }

            var target = $('[data-widget-id=' + widgetId + ']');
            var attr = {
                id : target.attr('id'),
                src : target.attr('src'),
                alt : target.attr('alt'),
                width : target.attr('width'),
                height : target.attr('height'),
                widgetId : widgetId,
                widgetType : 'jmImage'
            };

            return attr;
        }
    };
})();
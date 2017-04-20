jmSelect = (function() {
    return {
        name : '下拉框',
        type : 'select',
        htmlSnippet : '<select id="${id}" class="${clazz}" name="${name}" value="${value}" data-widget-id="${widgetId}" data-widget-type="jmSelect">'
                + '<option value="">===请选择===</option>' + '</select>',
        attributes : [ {
            name : 'id',
            value : 'id',
            description : 'the id of the select'
        }, {
            name : 'name',
            value : 'name',
            description : 'the name of the select'
        }, {
            name : 'value',
            value : 'value',
            description : 'the value of the select'
        }, {
            name : 'options',
            value : 'options',
            description : 'key1=val1;key2=val2'
        } ],
        initHtml : function(attrs) {
            var defaultAttrs = {
                id : '',
                name : '',
                value : '',
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

                if (key == 'value') {
                    target.val(value);
                } else if (key == 'options') {
                    var options = '<option value="">===请选择===</option>';
                    var pairs = value.split(';');

                    $.each(pairs, function(i, pair) {
                        if (pair.length > 0 && pair.indexOf('=') > -1) {
                            var kv = pair.split('=');
                            options += '<option value="' + kv[0] + '">' + kv[1] + '</option>';
                        }
                    });
                    
                    var originVal = target.val();
                    target.html(options);

                    if (widgetUtils.isString(originVal)) {
                        target.val(originVal);
                    }
                } else {
                    target.attr(key, value);
                }
            }
        },
        getAttr : function(widgetId) {
            if (!widgetUtils.isString(widgetId) || widgetId.length == 0) {
                console.log('widgetId cannot be empty');
                return;
            }

            var target = $('[data-widget-id=' + widgetId + ']');

            var options = '';
            $.each(target.children('option'), function(i, opn) {
                if (i == 0) {
                    return;
                }

                var $opn = $(opn);
                options += $opn.attr('value') + '=' + $opn.text() + ';';
            });

            var attr = {
                id : target.attr('id'),
                name : target.attr('name'),
                value : target.val(),
                options : options,
                widgetId : widgetId,
                widgetType : 'jmSelect'
            };

            return attr;
        }
    };
})();
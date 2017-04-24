jmProgress = (function() {
    var template = '<div id="${id}" class="${clazz}" data-widget-id="${widgetId}" data-widget-type="jmProgress">' +
                        '<div class="progress-bar" role="progressbar" aria-valuenow="${nowValue}" ' +
                            'aria-valuemin="${minValue}" aria-valuemax="${maxValue}" style="width: 10%;">' +
                            '<span class="sr-only">10% 完成</span>' +
                        '</div>' +
                    '</div>';
    

        return {
        name : '进度条',
        type : 'progress',
        htmlSnippet : template,
        attributes : [ {
            name : 'id',
            value : 'id',
            description : 'the id of the progress'
        }, {
            name : 'minValue',
            value : 'minValue',
            description : 'the minValue of the progress'
        }, {
            name : 'maxValue',
            value : 'maxValue',
            description : 'the maxValue of the progress'
        }, {
            name : 'nowValue',
            value : 'nowValue',
            description : 'the nowValue of the progress'
        } ],
        initHtml : function(attrs) {
            var defaultAttrs = {
                id : '',
                minValue : 0,
                maxValue : 100,
                nowValue : 10,
                clazz : 'target progress',
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
            var progress = target.children('.progress-bar');

            for ( var key in attrs) {
                if (key == 'widgetId') {
                    continue;
                }

                var value = attrs[key];

                if (!widgetUtils.isString(value)) {
                    continue;
                }

                if (key === 'id') {
                    target.attr(key, value);
                }

                if (key === 'minValue') {
                    if (isNaN(parseInt(value))) {
                        console.log('minValue must be a int number');
                        continue;
                    }

                    progress.attr('aria-valuemin', value);
                }

                if (key === 'maxValue') {
                    if (isNaN(parseInt(value))) {
                        console.log('maxValue must be a int number');
                        continue;
                    }

                    progress.attr('aria-valuemax', value);
                }

                if (key === 'nowValue') {
                    if (isNaN(parseInt(value))) {
                        console.log('nowValue must be a int number');
                        continue;
                    }

                    progress.attr('aria-valuenow', value);
                }

                var minValue = parseInt(progress.attr('aria-valuemin'));
                var maxValue = parseInt(progress.attr('aria-valuemax'));
                var nowValue = parseInt(progress.attr('aria-valuenow'));
                var pro = nowValue * 100 / (maxValue - minValue);
                progress.attr('style', 'width: ' + pro + '%;');
                progress.children('.sr-only').text(pro + '% 完成')
            }
        },
        getAttr : function(widgetId) {
            if (!widgetUtils.isString(widgetId) || widgetId.length == 0) {
                console.log('widgetId cannot be empty');
                return;
            }

            var target = $('[data-widget-id=' + widgetId + ']');
            var progress = target.children('.progress-bar');
            var attr = {
                id : target.attr('id'),
                minValue : progress.attr('aria-valuemin'),
                maxValue : progress.attr('aria-valuemax'),
                nowValue : progress.attr('aria-valuenow'),
                widgetId : widgetId,
                widgetType : 'jmProgress'
            };

            return attr;
        }
    };
})();
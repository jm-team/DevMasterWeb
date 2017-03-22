/**
 * 
 */
function myLoadFilter(data, parent){
    var state = $.data(this, 'tree');
    
    function setData(){
        var serno = 1;
        var todo = [];
        for(var i=0; i<data.length; i++){
            todo.push(data[i]);
        }
        while(todo.length){
            var node = todo.shift();
            if (node.id == undefined){
                node.id = '_node_' + (serno++);
            }
            if (node.children){
                node.state = 'closed';
                node.children1 = node.children;
                node.children = undefined;
                todo = todo.concat(node.children1);
            }
        }
        state.tdata = data;
    }
    function find(id){
        var data = state.tdata;
        var cc = [data];
        while(cc.length){
            var c = cc.shift();
            for(var i=0; i<c.length; i++){
                var node = c[i];
                if (node.id == id){
                    return node;
                } else if (node.children1){
                    cc.push(node.children1);
                }
            }
        }
        return null;
    }
    
    setData();
    
    var t = $(this);
    var opts = t.tree('options');
    opts.onBeforeExpand = function(node){
        var n = find(node.id);
        if (n.children && n.children.length){return}
        if (n.children1){
            var filter = opts.loadFilter;
            opts.loadFilter = function(data){return data;};
            t.tree('append',{
                parent:node.target,
                data:n.children1
            });
            opts.loadFilter = filter;
            n.children = n.children1;
        }
    };
    return data;
}

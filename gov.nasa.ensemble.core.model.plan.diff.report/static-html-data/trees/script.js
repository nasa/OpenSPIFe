function select_tree(tree_id) {
	var trees = document.getElementsByName("TopLevelTree");
	for (var i = 0; i < trees.length; i++) {
	   trees[i].style.display = trees[i].id==tree_id? "block" : "none";
	}
}

function show_children (event) {
        target = event.target.parentNode; 
        target.className = target.className.replace('collapsed', 'expanded');
}

function hide_children (event) {
        target = event.target.parentNode; 
        target.className = target.className.replace('expanded', 'collapsed');
}

function expand_all () {
   globally_change_subtree_class('collapsed', 'expanded');
   }
   
function collapse_all () {
   globally_change_subtree_class('expanded', 'collapsed');
   }

function globally_change_subtree_class(oldclass, newclass) {
	    var nodes = document.getElementsByName("subtree");
	    for (var i = 0; i < nodes.length; i++) {
   	       nodes[i].className = nodes[i].className.replace(oldclass, newclass);
   	    }
   }
   
function expand_top_level () {
   collapse_all();
   change_class_top_level('collapsed', 'expanded');
   }

function change_class_top_level (oldclass, newclass) {
    var trees = document.getElementsByName("TopLevelTree");
    for (var j = 0; j < trees.length; j++) {
        var ul_nodes = trees[j].childNodes;
        for (var i = 0; i < ul_nodes.length; i++) {
            try { // skip text nodes
                var li_nodes = ul_nodes[i].childNodes;
                for (var k = 0; k < li_nodes.length; k++) {
                    try { // skip text nodes
                        li_nodes[k].className = li_nodes[k].className.replace(oldclass, newclass);
                    } catch (e) {}
                }
            } catch (e) {}
        }
    }
}

function show_nodes_of_class(className) {
	globally_change_subtree_class("hidden " + className, className);
}

function hide_nodes_of_class(className) {
	globally_change_subtree_class(className, "hidden " + className);
}

function highlight_row(target, color) {
  if (target==null) return;
  if (target==document.body) return;
  if (typeof(target.getAttribute) != "function") return;
  if (target.getAttribute("name")=="subtree") return target.style.backgroundColor=color;
  highlight_row(target.parentNode, color);
}

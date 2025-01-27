<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Page Size Diagram</title>


    <script type="text/javascript" src="https://www.basicprimitives.com/api/javascript/primitives.js"></script>
    <link href="https://www.basicprimitives.com/api/javascript/css/primitives.css" media="screen" rel="stylesheet" type="text/css" />

    <script type="text/javascript">
        var control;
        var timer = null;
        document.addEventListener('DOMContentLoaded', function () {
            var options = new primitives.OrgConfig();

            var items = [
                new primitives.OrgItemConfig({
                    id: 0,
                    parent: null,
                    title: "Title 0",
                    description: "Description",
                    image: "https://www.basicprimitives.com/api/images/photos/a.png"
                })
            ];

            var id = 1;
            for (var index = 0; index < 2; index++) {
                items.push(new primitives.OrgItemConfig({
                    id: ++id,
                    parent: 0,
                    title: id.toString() + " Title",
                    description: id.toString() + " Description",
                    image: "https://www.basicprimitives.com/api/images/photos/c.png"
                }));
                var parent = id;
                for (var index2 = 0; index2 < 2; index2++) {
                    items.push(new primitives.OrgItemConfig({
                        id: ++id,
                        parent: parent,
                        title: id.toString() + " Title",
                        description: id.toString() + " Description",
                        image: "https://www.basicprimitives.com/api/images/photos/c.png"
                    }));
                }
            }

            options.pageFitMode = primitives.PageFitMode.None;
            options.items = items;
            options.cursorItem = 0;
            options.hasSelectorCheckbox = primitives.Enabled.True;

            control = primitives.OrgDiagram(document.getElementById("basicdiagram"), options);

            window.addEventListener('resize', function (event) {
                onWindowResize();
            });
        });

        function onWindowResize() {
            if (timer == null) {
                timer = window.setTimeout(function () {
                    control.update(primitives.UpdateMode.Refresh);
                    window.clearTimeout(timer);
                    timer = null;
                }, 300);
            }
        }
    </script>
</head>
<body style="overflow:hidden;">
<div id="basicdiagram" style="position: absolute; border-style: dotted; border-width: 0px; top: 0; right: 0; bottom: 0; left: 0;"></div>
</body>
</html>

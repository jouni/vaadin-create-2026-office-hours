package com.example.ui;

import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "")
@RouteAlias(value = "/:rest*")
@PageTitle("Example View")
class ExampleView extends Main {

    ExampleView() {
        addClassNames("example-view");
        add(new ViewHeader());
    }

}

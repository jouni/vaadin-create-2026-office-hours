package com.example.ui;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.breadcrumbs.Breadcrumbs;
import com.vaadin.flow.component.breadcrumbs.BreadcrumbsItem;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

@StyleSheet("./view-header.css")
public class ViewHeader extends Header implements BeforeEnterObserver {
  private Breadcrumbs breadcrumbs = new Breadcrumbs(Breadcrumbs.Mode.MANUAL);

  ViewHeader() {
    addClassNames("view-header");
    // breadcrumbs.addThemeVariants(BreadcrumbsVariant.SLASH);
    add(new DrawerToggle(), breadcrumbs);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    breadcrumbs.removeAll();

    var path = event.getLocation().getPath();

    if (path.equals("")) {
      breadcrumbs.add(new BreadcrumbsItem("Home"));
    } else {
      String[] segments = Arrays.stream(path.split("/"))
          .filter(s -> !s.isBlank())
          .toArray(String[]::new);
      String[] titles = routeToTitles(path);

      StringBuilder href = new StringBuilder();
      for (int i = 0; i < titles.length; i++) {
        href.append('/').append(segments[i]);
        boolean isLast = i == titles.length - 1;
        breadcrumbs.add(isLast
            ? new BreadcrumbsItem(titles[i])
            : new BreadcrumbsItem(titles[i], href.toString()));
      }
    }
  }

  public static String[] routeToTitles(String route) {
    return Arrays.stream(route.split("/"))
        .filter(segment -> !segment.isBlank())
        .map(segment -> Arrays.stream(segment.split("-"))
            .filter(word -> !word.isEmpty())
            .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
            .collect(Collectors.joining(" ")))
        .toArray(String[]::new);
  }
}

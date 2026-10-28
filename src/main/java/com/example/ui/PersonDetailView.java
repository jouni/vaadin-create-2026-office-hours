package com.example.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

@Route(value = "people/:personId", layout = PeopleView.class)
public class PersonDetailView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

    private PeopleView.Person person;

    public PersonDetailView() {
        addClassName("person-detail-view");
        setPadding(true);
        setSpacing(true);
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var selectedPerson = event.getRouteParameters()
                .get("personId")
                .flatMap(PeopleView::findPersonById);

        if (selectedPerson.isEmpty()) {
            event.rerouteTo(PeopleView.class);
            return;
        }

        person = selectedPerson.get();
        renderPerson();
    }

    @Override
    public String getPageTitle() {
        return person == null ? "Person details · My App" : person.name() + " · My App";
    }

    private void renderPerson() {
        removeAll();
        add(createToolbar(), createIdentity(), createDetailsCard(), createActions());
    }

    private Component createToolbar() {
        var title = new H2("Person details");
        title.addClassName("person-detail-title");

        var close = new Button(LucideIcon.X.create());
        close.addThemeVariants(ButtonVariant.TERTIARY);
        close.setAriaLabel("Close person details");
        close.addClickListener(event -> UI.getCurrentOrThrow().navigate(PeopleView.class));

        var toolbar = new HorizontalLayout(title, close);
        toolbar.addClassName("person-detail-toolbar");
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setWidthFull();
        return toolbar;
    }

    private Component createIdentity() {
        var avatar = new Avatar(person.name());
        avatar.setColorIndex(person.colorIndex());
        avatar.addThemeVariants(AvatarVariant.XLARGE);

        var name = new H2(person.name());
        var email = new Paragraph(person.email());

        var status = new Badge(person.status());
        status.addThemeVariants(BadgeVariant.SMALL);
        switch (person.status()) {
            case "Active" -> status.addThemeVariants(BadgeVariant.SUCCESS);
            case "On leave" -> status.addThemeVariants(BadgeVariant.WARNING);
            default -> { }
        }

        var copy = new VerticalLayout(name, email, status);
        copy.addClassName("person-detail-identity-copy");
        copy.setPadding(false);
        copy.setSpacing(false);

        var identity = new HorizontalLayout(avatar, copy);
        identity.addClassName("person-detail-identity");
        identity.setAlignItems(Alignment.CENTER);
        identity.setWidthFull();
        return identity;
    }

    private Component createDetailsCard() {
        var details = new FormLayout();
        details.add(
                readOnlyField("Team", person.team()),
                readOnlyField("Location", person.location()),
                readOnlyField("Start date", person.startDate()),
                readOnlyField("Status", person.status()),
                readOnlyField("Email", person.email()));
        details.setWidthFull();

        var card = new Card();
        card.setTitle("Work profile");
        card.addThemeVariants(CardVariant.OUTLINED);
        card.add(details);
        card.addClassName("person-detail-card");
        card.setWidthFull();
        return card;
    }

    private TextField readOnlyField(String label, String value) {
        var field = new TextField(label, value);
        field.setReadOnly(true);
        field.setWidthFull();
        return field;
    }

    private Component createActions() {
        var edit = new Button("Edit profile");
        edit.addThemeVariants(ButtonVariant.PRIMARY);

        var message = new Button("Send message");
        var actions = new HorizontalLayout(edit, message);
        actions.addClassName("person-detail-actions");
        actions.setWidthFull();
        return actions;
    }
}

/*
 * Copyright (C) 2014 Language In Interaction
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package nl.ru.languageininteraction.language.client;

import nl.ru.languageininteraction.language.client.listener.AppEventListner;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.logging.Logger;
import nl.ru.languageininteraction.language.client.exception.AudioException;
import nl.ru.languageininteraction.language.client.presenter.AlienScreen;
import nl.ru.languageininteraction.language.client.presenter.AutotypRegionsMapScreen;
import nl.ru.languageininteraction.language.client.presenter.Presenter;
import nl.ru.languageininteraction.language.client.presenter.ErrorPresenter;
import nl.ru.languageininteraction.language.client.presenter.MatchLanguagePresenter;
import nl.ru.languageininteraction.language.client.presenter.VersionPresenter;
import nl.ru.languageininteraction.language.client.model.UserResults;
import nl.ru.languageininteraction.language.client.presenter.InstructionsPresenter;
import nl.ru.languageininteraction.language.client.presenter.IntroPresenter;
import nl.ru.languageininteraction.language.client.presenter.LocalePresenter;
import nl.ru.languageininteraction.language.client.presenter.MapPresenter;
import nl.ru.languageininteraction.language.client.presenter.MetadataPresenter;
import nl.ru.languageininteraction.language.client.presenter.UserNamePresenter;
import nl.ru.languageininteraction.language.client.service.LocalStorage;

/**
 * @since Oct 7, 2014 11:07:35 AM (creation date)
 * @author Peter Withers <p.withers@psych.ru.nl>
 */
public class AppController implements AppEventListner {

    private static final Logger logger = Logger.getLogger(AppController.class.getName());

    private final RootPanel widgetTag;
    private Presenter presenter;
    private final UserResults userResults;
//    private final StimuliProvider stimuliProvider;

    public AppController(RootPanel widgetTag) {
        this.widgetTag = widgetTag;
//        stimuliProvider = new StimuliProvider();
        userResults = new LocalStorage().getStoredData();
//        userResults.setPendingStimuliGroup(stimuliProvider.getDefaultStimuli());
    }

    @Override
    public void requestApplicationState(ApplicationState applicationState) {
        try {
            trackView(applicationState.name());
            switch (applicationState) {
//                case menu:
//                    userResults.setPendingStimuliGroup(null);
//                    this.presenter = new MenuPresenter(widgetTag);
//                    presenter.setState(this, null, null);
//                    break;
                case locale:
                    this.presenter = new LocalePresenter(widgetTag);
                    presenter.setState(this, null, null);
                    break;
                case version:
                    this.presenter = new VersionPresenter(widgetTag);
                    presenter.setState(this, ApplicationState.match, null);
                    break;
                case match:
                    this.presenter = new MatchLanguagePresenter(widgetTag);
                    presenter.setState(this, ApplicationState.version, ApplicationState.map);
                    break;
                case map:
                    this.presenter = new MapPresenter(widgetTag);
                    presenter.setState(this, ApplicationState.version, ApplicationState.moreinfo);
                    break;
                case moreinfo:
                    this.presenter = new InstructionsPresenter(widgetTag);
                    presenter.setState(this, ApplicationState.map, ApplicationState.autotyp_regions);
                    break;
                case autotyp_regions:
                    this.presenter = new AutotypRegionsMapScreen(widgetTag);
                    presenter.setState(this, ApplicationState.moreinfo, ApplicationState.alien);
                    break;
                case alien:
                    this.presenter = new AlienScreen(widgetTag);
                    presenter.setState(this, ApplicationState.version, ApplicationState.map);
                    break;
                case start:
                case intro:
                    this.presenter = new IntroPresenter(widgetTag);
                    presenter.setState(this, null, ApplicationState.setuser);
                    break;
                case setuser:
                    this.presenter = new UserNamePresenter(widgetTag, userResults);
                    presenter.setState(this, null, ApplicationState.match);
                    ((MetadataPresenter) presenter).focusFirstTextBox();
                    break;
//                case stimulus:
//                    if (userResults.getPendingStimuliGroup() == null) {
//                        this.presenter = new StimulusMenuPresenter(widgetTag, stimuliProvider, userResults);
//                        presenter.setState(this, ApplicationState.start, ApplicationState.report);
//                    } else {
//                        trackEvent(applicationState.name(), "show", userResults.getPendingStimuliGroup().getGroupLabel());
//                        this.presenter = new ColourPickerPresenter(widgetTag, userResults, 3);
//                        presenter.setState(this, null, ApplicationState.stimulus);
//                    }
//                    break;
//                case adddummyresults:
//                    final StimuliGroup[] stimuli = stimuliProvider.getStimuli();
//                    userResults.addDummyResults(stimuli[0]);
//                    userResults.addDummyResults(stimuli[1]);
//                    userResults.addDummyResults(stimuli[2]);
//                case report:
//                    this.presenter = new ReportPresenter(widgetTag, userResults);
//                    presenter.setState(this, null, ApplicationState.feedback);
//                    break;
//                case feedback:
//                    this.presenter = new FeedbackPresenter(widgetTag);
//                    presenter.setState(this, ApplicationState.report, ApplicationState.metadata);
//                    break;
//                case metadata:
//                    this.presenter = new MetadataPresenter(widgetTag, userResults);
//                    presenter.setState(this, null, ApplicationState.registration);
//                    ((MetadataPresenter) presenter).focusFirstTextBox();
//                    break;
//                case registration:
//                    if (userResults.getStimuliGroups().isEmpty()) {
//                        this.presenter = new RegisterDisabledPresenter(widgetTag);
//                        presenter.setState(this, null, ApplicationState.stimulus);
//                    } else {
//                        this.presenter = new RegisterPresenter(widgetTag, userResults);
//                        presenter.setState(this, null, ApplicationState.moreinfo);
//                    }
//                    break;
//                case moreinfo:
//                    this.presenter = new MoreInfoPresenter(widgetTag);
//                    presenter.setState(this, ApplicationState.start, null);
//                    break;
                case end:
                    exitApplication();
                    break;
                default:
                    this.presenter = new ErrorPresenter(widgetTag, "No state for: " + applicationState);
                    presenter.setState(this, ApplicationState.start, applicationState);
                    break;
            }
        } catch (AudioException error) {
            logger.warning(error.getMessage());
            this.presenter = new ErrorPresenter(widgetTag, error.getMessage());
            presenter.setState(this, ApplicationState.start, applicationState);
        }
    }

    public void start() {
        setBackButtonAction();
        requestApplicationState(AppEventListner.ApplicationState.start);
    }

    public void backAction() {
        presenter.fireBackEvent();
    }

    public static native void trackView(String applicationState) /*-{
     if($wnd.analytics) $wnd.analytics.trackView(applicationState);
     }-*/;

    public static native void trackEvent(String applicationState, String label, String value) /*-{
     if($wnd.analytics) $wnd.analytics.trackEvent(applicationState, "view", label, value);
     }-*/;

    private native void setBackButtonAction() /*-{
     var appController = this;
     $doc.addEventListener("backbutton", function(e){
     e.preventDefault();
     appController.@nl.ru.languageininteraction.language.client.AppController::backAction()();
     }, false);
     }-*/;

    private native void exitApplication() /*-{
     $doc.navigator.app.exitApp();
     }-*/;
}

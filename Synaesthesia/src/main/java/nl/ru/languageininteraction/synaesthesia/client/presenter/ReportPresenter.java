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
package nl.ru.languageininteraction.synaesthesia.client.presenter;

import nl.ru.languageininteraction.synaesthesia.client.view.ReportView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import nl.ru.languageininteraction.synaesthesia.client.AppEventListner;
import nl.ru.languageininteraction.synaesthesia.client.Messages;
import nl.ru.languageininteraction.synaesthesia.client.Presenter;
import nl.ru.languageininteraction.synaesthesia.client.PresenterEventListner;
import nl.ru.languageininteraction.synaesthesia.client.ScoreCalculator;
import nl.ru.languageininteraction.synaesthesia.client.UserResults;

/**
 * @since Oct 14, 2014 10:57:39 AM (creation date)
 * @author Peter Withers <p.withers@psych.ru.nl>
 */
public class ReportPresenter implements Presenter {

    private final Messages messages = GWT.create(Messages.class);
    private final RootPanel widgetTag;
    private final UserResults userResults;

    public ReportPresenter(RootPanel widgetTag, UserResults userResults) {
        this.widgetTag = widgetTag;
        this.userResults = userResults;
    }

    @Override
    public void setState(final AppEventListner appEventListner, final AppEventListner.ApplicationState prevState, final AppEventListner.ApplicationState nextState) {
        widgetTag.clear();
        final ReportView reportView = new ReportView();
        if (prevState != null) {
            reportView.setButton(messages.prevbutton(), new PresenterEventListner() {

                @Override
                public void eventFired() {
                    appEventListner.requestApplicationState(prevState);
                }

            });
        }
        if (nextState != null) {
            reportView.setButton(messages.nextbutton(), new PresenterEventListner() {

                @Override
                public void eventFired() {
                    appEventListner.requestApplicationState(nextState);
                }

            });
        }
        final ScoreCalculator scoreCalculator = new ScoreCalculator(userResults);
        reportView.addTitle(messages.reportScreenTitle());
        reportView.showResults(userResults, scoreCalculator);
        reportView.addText(messages.reportScreenScore(Double.toString(scoreCalculator.getScore())));
        reportView.addText(messages.reportScreenPostScoreText());
        reportView.addText(messages.reportScreenSCT());
        reportView.addText(messages.reportScreenSCTaccuracy(Double.toString(scoreCalculator.getAccuracy())));
        reportView.addText(messages.reportScreenSCTmeanreactionTime(Double.toString(scoreCalculator.getMeanReactionTime()), Double.toString(scoreCalculator.getReactionTimeDeviation())));
        reportView.addText(messages.reportScreenPostSCTtext());
        reportView.resizeView();
        widgetTag.add(reportView);
    }

}

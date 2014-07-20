/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.panel.paintWorker;

import gui.panel.JPanelVideoPlayer;

/**
 *
 * @author nick
 */
public interface IWorkerInterface {

    void setTopPanel(JPanelVideoPlayer aThis);
    void execute();
    void stopExecuting();
}

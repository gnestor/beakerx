package com.twosigma.beaker.jupyter.threads;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.twosigma.beaker.jvm.object.SimpleEvaluationObject;
import com.twosigma.jupyter.KernelFunctionality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twosigma.beaker.jupyter.msg.MessageCreator;
import com.twosigma.beaker.jupyter.msg.MessageHolder;
import com.twosigma.beaker.jupyter.SocketEnum;

public class ExecutionResultSender implements Observer {

  public static Logger logger = LoggerFactory.getLogger(ExecutionResultSender.class);

  private MessageCreator handler;
  private final ConcurrentLinkedQueue<MessageHolder> messageQueue = new ConcurrentLinkedQueue<>();
  private AbstractThread workingThread;
  private KernelFunctionality kernel;

  public ExecutionResultSender(KernelFunctionality kernel) {
    this.kernel = kernel;
    handler = new MessageCreator(kernel);
  }

  @Override
  public synchronized void update(Observable o, Object arg) {
    SimpleEvaluationObject seo = (SimpleEvaluationObject) o;
    if (seo != null) {
      messageQueue.addAll(handler.createMessage(seo));
      if (workingThread == null || !workingThread.isAlive()) {
        workingThread = new MessageRunnable();
        workingThread.start();
      }
    }
  }

  protected class MessageRunnable extends AbstractThread {

    @Override
    public boolean getRunning() {
      return running && !messageQueue.isEmpty();
    }

    @Override
    public void run() {
      while (getRunning()) {
        MessageHolder job = messageQueue.poll();

        if (handler != null && job != null) {
          if (SocketEnum.IOPUB_SOCKET.equals(job.getSocketType())) {
            kernel.publish(job.getMessage());
          } else if (SocketEnum.SHELL_SOCKET.equals(job.getSocketType())) {
            kernel.send(job.getMessage());
          }
        }
      }
      logger.info("ShellThread shutdown.");
    }
  }

  public void exit() {
    if (workingThread != null) {
      workingThread.halt();
    }
  }

}
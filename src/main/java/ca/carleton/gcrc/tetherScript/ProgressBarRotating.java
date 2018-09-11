package ca.carleton.gcrc.tetherScript;
class ProgressBarRotating extends Thread {
	  boolean showProgress = true;
	  public void run() {
	    String anim []= {
	    		         ">-----------------",
	    		         "->----------------",
	    		         "-->---------------",
	    		         "--->--------------",
	    		         "---->-------------",
	    		         "----->------------",
	    		         "------>-----------",
	    				 "------->------- --",
	    				 "-------->---------",
	    		         "--------->--------",
	    		         "---------->-------",
	    		         "----------->------",
	    		         "------------>-----",
	    		         "------------->----",
	    		         "-------------->---",
	    		         "--------------->--",
	    		         "---------------->-",
	    		         "----------------->",};
	    int x = 0;
	    while (showProgress) {
	      System.out.print("\r Processing |" + anim[x++ % anim.length]);
	      try { Thread.sleep(100); }
	      catch (Exception e) {};
	    }
	  }
}
	  
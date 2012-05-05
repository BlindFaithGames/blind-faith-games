package es.eucm.blindfaithgames.minesweeper;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class EntryReceiver extends Receiver<Long>{

	Long entryId = new Long(-1);
   @Override
   public void onFailure(ServerFailure error) {
       error.getMessage();
   }
   
   @Override
   public void onSuccess(Long e) {
       entryId = new Long(e);
   }

   public Long getResult(){
   	return entryId;
   }
}
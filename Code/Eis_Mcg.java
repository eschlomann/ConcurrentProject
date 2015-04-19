public class Eis_Mcg {
	private int n;
	private int out_cr 		= 0;
	private int want_cr 	= 1;
	private int claim_cr 	= 2;
	private volatile int turn;
	private volatile int[] flags;

	public Eis_Mcg(int nproc){
		this.n = nproc;
		this.flags = new int[nproc];
		this.turn = nproc - 1;
		for(int i = 0; i < this.n; i++){
			this.flags[i] = out_cr;
		}
	}

	public void lock(int ID){
		this.flags[ID] = want_cr;
		int index = this.turn;
		do{
			while(index != ID){
				if(this.flags[index] == out_cr){
					index = (index + 1) % this.n;
				}
				else
					index = this.turn;
			}
			this.flags[ID] = claim_cr;

			index = 0;
			while((index < this.n) && ((index == ID) || (this.flags[ID] != claim_cr))){
				index++;
			}

		} while((index >= this.n) && ((this.turn == ID) || (this.flags[this.turn] == out_cr)));
		this.turn = ID;
	}

	public void unlock(int ID){
		int index = (this.turn + 1) % this.n;
		while(this.flags[index] == out_cr)
			index = (index + 1) % this.n;
		this.turn = index;
		this.flags[ID] = out_cr;
	}
}









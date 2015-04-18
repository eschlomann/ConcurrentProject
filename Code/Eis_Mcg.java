public class Eis_Mcg {
	private int n;
	private int out_cr 		= 0;
	private int want_cr 	= 1;
	private int claim_cr 	= 2;
	// private enum procphase { out_cr, want_cr, claim_cr};
	private int turn;
	private int[] procphase;

	public Eis_Mcg(int nproc){

		this.n = nproc;
		procphase = new int[nproc];

		this.turn = nproc - 1;

		for(int i = 0; i < this.n; i++)
			procphase[i] = out_cr;
	}

	public void lock(int ID){
		procphase[ID] = want_cr;
		int j = this.turn;
		do{
			while(j != ID){
				if(procphase[j] == out_cr)
					j = (j + 1) % this.n;
				else
					j = this.turn;
			}
			procphase[ID] = claim_cr;
			j = (j + 1) % this.n;
			while(procphase[j] != claim_cr)
				j = (j + 1) % this.n;
		}
		while(!(j == ID && (turn == ID || procphase[this.turn] == out_cr)));
		this.turn = ID;
	}

	public void unlock(int ID){
		int j = (this.turn + 1) % this.n;
		while(procphase[j] == out_cr)
			j = (j + 1) % this.n;
		this.turn = j;
		procphase[ID] = out_cr;
	}
}









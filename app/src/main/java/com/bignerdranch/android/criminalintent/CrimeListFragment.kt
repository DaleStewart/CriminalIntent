package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"

class CrimeListFragment: Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    private val crimeListViewModel: CrimeListViewModel by lazy{
        ViewModelProviders.of(this).get(crimeListViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        //get a reference to our recycler view and set up a layout manager
        crimeRecyclerView = view.findViewById(R.id.crime_recycle_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)//need a layout manager

       return view
        updateUI()
    }

    //this is the proverbial 'first domino'
    //when it is called the crime list is snagged from the view model and passed to
    //an adapter through the constructor
    //lastly the recyclerview adapter property is set to the adapter we just made
    private fun updateUI(){
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    //A recycler view never creates views directly, it interacts with view holders
    private inner class CrimeHolder(view: View):RecyclerView.ViewHolder(view){

        private lateinit var crime: Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

        fun bind(crime: Crime){
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }
    }

    //need a crime adapter to create the viewHolders
    private inner class CrimeAdapter(var crimes: List<Crime>):RecyclerView.Adapter<CrimeHolder>(){

        //overriden adapter class method
        //this method creates the view to display, wraps it in a view holder, and returns the result
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        //overriden adapter class method to get the number of objects we have to produce
        override fun getItemCount(): Int {
            return crimes.size
        }

        //overridden adapter class method
        //populates the given view holder with the object at the given postion
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }
    }

    companion object {
        fun newInstance(): CrimeListFragment{
            return CrimeListFragment()
        }
    }
}
package com.bignerdranch.android.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment: Fragment() {

    /*
    * Required interface for hosting activities
     */
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }
    //----------------------------------------------------------------------------------------------

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.new_crime -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        //get a reference to our recycler view and set up a layout manager
        crimeRecyclerView = view.findViewById(R.id.crime_recycle_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)//need a layout manager
        crimeRecyclerView.adapter = adapter//set the recycler view adapter

       //updateUI() //CC1
       return view
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes $crimes.size")
                    updateUI(crimes)
                }
            }
        )
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
    //----------------------------------------------------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }


    //----------------------------------------------------------------------------------------------
    //this is the proverbial 'first domino'
    //when it is called the crime list is snagged from the view model and passed to
    //an adapter through the constructor
    //lastly the recyclerview adapter property is set to the adapter we just made
    private fun updateUI(crimes: List<Crime>){
       //val crimes = crimeListViewModel.crimes//CC1
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //CLASS: crime holder is an extension of recyclerview.viewholder for holding itemViews
    private inner class CrimeHolder(view: View):RecyclerView.ViewHolder(view), View.OnClickListener{

        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime){
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
            solvedImageView.visibility = if(crime.isSolved){
                View.VISIBLE
            } else{
                View.GONE
            }
        }

        override fun onClick(v: View){
            //Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onCrimeSelected(crime.id)
        }
    }
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //CLASS extension of Adapter creates and manages ViewHolders for the recycler view
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
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    companion object {
        fun newInstance(): CrimeListFragment{
            return CrimeListFragment()
        }
    }
    //----------------------------------------------------------------------------------------------
}
package com.spezia.spezia.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spezia.spezia.api.api_responses.dictionary.DictionaryApiModel
import com.spezia.spezia.databinding.SpicesListItemRowBinding
import com.spezia.spezia.view.dictionary.SpicesDictionaryDetailsActivity

class ListSpicesDictionaryRVAdapter(
    private val listSpicesDictionary : ArrayList<DictionaryApiModel>) :
    RecyclerView.Adapter<ListSpicesDictionaryRVAdapter.ListSpicesDictionaryViewHolder>() {

    inner class ListSpicesDictionaryViewHolder(
        private var binding : SpicesListItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(dictionaryModel : DictionaryApiModel) {
                binding.apply {
                    tvItemSpiceName.text = dictionaryModel.name
                    tvItemShortDesc.text = dictionaryModel.description
                    Glide.with(itemView.context)
                        .load(dictionaryModel.image)
                        .circleCrop()
                        .into(imgItemPhoto)

                    val spicesMetadata = DictionaryApiModel(
                        dictionaryModel.spiceId,
                        dictionaryModel.name,
                        dictionaryModel.latin_name,
                        dictionaryModel.image,
                        dictionaryModel.description,
                        dictionaryModel.benefits
                    )
                    Log.d("DictionaryModel : ", spicesMetadata.toString())

                    itemView.setOnClickListener {
                        val intentWithData = Intent(
                            itemView.context,
                            SpicesDictionaryDetailsActivity::class.java)
                            .putExtra(
                                SpicesDictionaryDetailsActivity.
                                EXTRA_SPICES_DICTIONARY_DETAILS,
                                dictionaryModel)

                        itemView.context.startActivity(
                            intentWithData,
                            ActivityOptionsCompat.
                            makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
                    }
                }
            }
    }

    override fun onCreateViewHolder(
        vg: ViewGroup,
        viewType: Int
    ): ListSpicesDictionaryViewHolder {
        val binding = SpicesListItemRowBinding.inflate(
            LayoutInflater.from(vg.context), vg, false)
        return ListSpicesDictionaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListSpicesDictionaryViewHolder, position: Int) {
        holder.bind(listSpicesDictionary[position])
    }

    override fun getItemCount(): Int {
        return listSpicesDictionary.size
    }


}
package com.spezia.spezia.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
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

                    val decodedBitmap = decodeBase64ToBitmap(dictionaryModel.image)
                    if (decodedBitmap != null) {
                        imgItemPhoto.setImageBitmap(decodedBitmap)
                    }

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
        val dictionaryModel = listSpicesDictionary[position]
        holder.bind(dictionaryModel)
    }

    override fun getItemCount(): Int {
        return listSpicesDictionary.size
    }

    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

}
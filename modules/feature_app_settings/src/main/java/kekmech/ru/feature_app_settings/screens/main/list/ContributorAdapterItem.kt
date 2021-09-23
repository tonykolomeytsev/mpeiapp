package kekmech.ru.feature_app_settings.screens.main.list

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.domain_app_settings.dto.GitHubUser
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.ItemContributorBinding

internal class ContributorAdapterItem(
    onClick: (GitHubUser) -> Unit,
) : AdapterItem<ContributorViewHolder, GitHubUser>(
    isType = { it is GitHubUser },
    layoutRes = R.layout.item_contributor,
    viewHolderGenerator = ::ContributorViewHolder,
    itemBinder = object : BaseItemBinder<ContributorViewHolder, GitHubUser>() {

        override fun bind(vh: ContributorViewHolder, model: GitHubUser, position: Int) {
            vh.setData(model)
            vh.setOnClickListener { onClick.invoke(model) }
        }
    }
)

internal class ContributorViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemContributorBinding.bind(itemView)

    fun setData(gitHubUser: GitHubUser) =
        with(viewBinding) {
            login.text = gitHubUser.login

            bio.isVisible = gitHubUser.bio != null
            gitHubUser.bio?.let(bio::setText)

            name.isVisible = gitHubUser.name != null
            gitHubUser.name?.let(name::setText)
        }

    fun setOnClickListener(onClick: () -> Unit) {
        viewBinding.root.setOnClickListener { onClick.invoke() }
    }
}
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    private val _refreshList = MutableStateFlow(false)
    val refreshList: StateFlow<Boolean> get() = _refreshList

    fun triggerListRefresh() {
        _refreshList.value = true
    }

    fun resetRefreshTrigger() {
        _refreshList.value = false
    }
}
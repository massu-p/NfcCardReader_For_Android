package jp.co.massu_p.nfccardreader.permission


import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import jp.co.massu_p.nfccardreader.R
import jp.co.massu_p.nfccardreader.dialog.TextAlertDialog
import jp.co.massu_p.nfccardreader.utils.PermissionUtils

/**
 * パーミッション確認用Fragment
 */
class PermissionFragment : Fragment() {

	companion object {
		private const val REQUEST_CODE_PERMISSION = 1
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return View(activity)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val requestPermissions = PermissionUtils.getRequestPermissions(requireContext())
		if (requestPermissions.isNotEmpty()) {
			ActivityCompat.requestPermissions(
				requireActivity(),
				requestPermissions,
				REQUEST_CODE_PERMISSION
			)
		} else {
			requireFragmentManager().beginTransaction().remove(this).commit()
		}
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		when(requestCode) {
			REQUEST_CODE_PERMISSION -> {
				for (result in grantResults) {
					if (result == PackageManager.PERMISSION_DENIED) {
						val dialog = TextAlertDialog(requireContext())
						dialog.title = getString(R.string.title_permission_denied)
						dialog.message = getString(R.string.message_permission_denied)
						dialog.onOkClickListener = DialogInterface.OnClickListener {_, _ ->
							ActivityCompat.requestPermissions(
								requireActivity(),
								permissions,
								REQUEST_CODE_PERMISSION
							)
						}
						dialog.showDialog(requireFragmentManager())
					}
					return
				}
				requireFragmentManager().beginTransaction().remove(this).commit()
			}
		}
	}
}

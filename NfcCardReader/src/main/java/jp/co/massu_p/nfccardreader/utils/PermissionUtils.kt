package jp.co.massu_p.nfccardreader.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionUtils {

	companion object {

		/**
		 * パーミッション一覧を取得する
		 * @return 確認が必須なパーミッション名のリスト
		 */
		fun getRequestPermissions(context: Context): Array<String> {
			val packageManager = context.packageManager
			val dangerPermissions = try {
				val packageInfo =
					packageManager.getPackageInfo(
						context.packageName,
						PackageManager.GET_PERMISSIONS
					)
				packageInfo.requestedPermissions
			} catch (e: PackageManager.NameNotFoundException) {
				// 権限確認が必要なパーミッションを記述しておく
				arrayOf(
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE
				)
			}

			val requestPermissions = mutableListOf<String>()
			for (permission in dangerPermissions) {
				if (ContextCompat.checkSelfPermission(
						context,
						permission
					) == PackageManager.PERMISSION_DENIED
				) {
					requestPermissions.add(permission)
				}
			}
			return requestPermissions.toTypedArray()
		}

		fun isGrantedExternalStorage(context: Context): Boolean {
			val permissions = arrayOf(
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE
			)
			var isGranted = true
			for (permission in permissions) {
				isGranted = isGranted && isGrantedPermission(context, permission)
			}
			return isGranted
		}

		fun isGrantedPermission(context: Context, permissionName: String): Boolean =
			ContextCompat.checkSelfPermission(
				context,
				permissionName
			) == PackageManager.PERMISSION_GRANTED
	}
}
# Implementation Plan - Lab 05: Fragments and Transfer History

Migrate the MobileBanking app from an Activity-based architecture to a single-Activity, multiple-Fragment architecture. Introduce local persistence with Room and list display with RecyclerView.

## User Review Required

> [!IMPORTANT]
> This change will consolidate all screens into `MainActivity`. `TransferActivity` will be deleted.
> I will be adding Room, Fragment-KTX, and RecyclerView dependencies.
> I will create new layouts for Dashboard, Confirmation, and History as they were missing in the current project state.

## Proposed Changes

### Dependencies & Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/gradle/libs.versions.toml)
- Add versions for `fragmentKtx`, `room`, `lifecycle`, and `recyclerview`.
- Define library aliases for these components.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/build.gradle.kts)
- Add the new dependencies.
- Ensure `kapt` is applied for Room's annotation processor.

### Data & Persistence

#### [MODIFY] [TransferRequest.kt](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/java/com/example/mobilebanking/TransferRequest.kt)
- Annotate with `@Entity`.
- Add an `id` primary key with `autoGenerate = true`.
- Implement `java.io.Serializable`.

#### [NEW] [TransferDao.kt](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/java/com/example/mobilebanking/TransferDao.kt)
- Define `@Dao` with `insert` and `getAll` methods.

#### [NEW] [AppDatabase.kt](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/java/com/example/mobilebanking/AppDatabase.kt)
- Define the Room database singleton.

### Layouts

#### [MODIFY] [activity_main.xml](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/res/layout/activity_main.xml)
- Replace content with `FragmentContainerView`.

#### [NEW] [fragment_dashboard.xml](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/res/layout/fragment_dashboard.xml)
- Create the dashboard UI.

#### [MODIFY] [fragment_transfer.xml](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/res/layout/activity_transfer.xml)
- Rename `activity_transfer.xml` to `fragment_transfer.xml`.

#### [NEW] [fragment_confirmation.xml](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/res/layout/fragment_confirmation.xml)
- Create the confirmation summary UI.

#### [NEW] [fragment_history.xml](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/res/layout/fragment_history.xml)
- Create the history screen with a `RecyclerView`.

#### [NEW] [item_transfer_history.xml](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/res/layout/item_transfer_history.xml)
- Define the layout for a single row in the history list.

### Fragments & Adapter

#### [NEW] [DashboardFragment.kt](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/java/com/example/mobilebanking/DashboardFragment.kt)
- Handle navigation to Transfer and History.

#### [NEW] [TransferFragment.kt](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/java/com/example/mobilebanking/TransferFragment.kt)
- Port form logic and validation from `TransferActivity`.

#### [NEW] [ConfirmationFragment.kt](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/java/com/example/mobilebanking/ConfirmationFragment.kt)
- Display summary and save to Room database.

#### [NEW] [HistoryFragment.kt](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/java/com/example/mobilebanking/HistoryFragment.kt)
- Fetch history from Room and set up `RecyclerView`.

#### [NEW] [TransferHistoryAdapter.kt](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/java/com/example/mobilebanking/TransferHistoryAdapter.kt)
- Manage the list of transfers in the UI.

### MainActivity & Cleanup

#### [MODIFY] [MainActivity.kt](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/java/com/example/mobilebanking/MainActivity.kt)
- Implement `show*Fragment` helper methods.
- Set `DashboardFragment` as the initial screen.

#### [DELETE] [TransferActivity.kt](file:///C:/Users/nethm/AndroidStudioProjects/MobileBanking/app/src/main/java/com/example/mobilebanking/TransferActivity.kt)
- Remove the redundant activity.

---

## Verification Plan

### Automated Tests
- Run Gradle sync.
- Execute `./gradlew :app:assembleDebug` to verify compilation.

### Manual Verification
- **Home Screen**: Launch app -> verify Dashboard is shown.
- **Transfer Flow**: Dashboard -> Transfer -> Validation -> Confirmation -> Dashboard.
- **History Flow**: Dashboard -> History -> Verify saved transfers are listed.
- **Back Stack**: Use system back button from Transfer/History -> verify return to Dashboard.

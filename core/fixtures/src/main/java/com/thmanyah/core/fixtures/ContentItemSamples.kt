package com.thmanyah.core.fixtures


import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection
import com.thmanyah.domain.models.ContentType
import com.thmanyah.domain.models.SectionLayout
import kotlinx.datetime.LocalDate

object ContentItemSamples {

    private val sampleImages = listOf(
        "https://picsum.photos/seed/alfarouk-a/400/400",
        "https://picsum.photos/seed/alfarouk-b/400/400",
        "https://picsum.photos/seed/alfarouk-c/400/400",
        "https://picsum.photos/seed/alfarouk-d/400/400",
    )

    fun podcast(
        id: String = "pod-1",
        title: String = "فنجان — بودكاست عربي",
        imageUrl: String? = sampleImages[0],
        durationSeconds: Long? = null,
        episodeCount: Int? = 12,
        language: String? = "ar",
        popularityScore: Int? = 8,
    ): ContentItem.Podcast = ContentItem.Podcast(
        id = id,
        title = title,
        imageUrl = imageUrl,
        durationSeconds = durationSeconds,
        episodeCount = episodeCount,
        language = language,
        popularityScore = popularityScore,
    )

    fun episode(
        id: String = "ep-1",
        title: String = "السفر الصامت وضوضاء الإعلانات",
        imageUrl: String? = sampleImages[1],
        durationSeconds: Long? = 1380L,
        audioUrl: String? = null,
        releaseDate: LocalDate? = LocalDate(2026, 5, 9),
        podcastName: String? = "شبكة راديو ثمانيه",
        authorName: String? = null,
    ): ContentItem.Episode = ContentItem.Episode(
        id = id,
        title = title,
        imageUrl = imageUrl,
        durationSeconds = durationSeconds,
        audioUrl = audioUrl,
        releaseDate = releaseDate,
        podcastName = podcastName,
        authorName = authorName,
    )

    fun episodeEnglish(
        id: String = "ep-en-1",
        title: String = "The Subscription Trap",
        imageUrl: String? = sampleImages[2],
        durationSeconds: Long? = 5400L,
        releaseDate: LocalDate? = LocalDate(2026, 5, 8),
        podcastName: String? = "Planet Money",
    ): ContentItem.Episode = ContentItem.Episode(
        id = id,
        title = title,
        imageUrl = imageUrl,
        durationSeconds = durationSeconds,
        audioUrl = null,
        releaseDate = releaseDate,
        podcastName = podcastName,
        authorName = null,
    )

    fun audioBook(
        id: String = "book-1",
        title: String = "صوت من التاريخ",
        imageUrl: String? = sampleImages[3],
        durationSeconds: Long? = 7260L,
        authorName: String? = "سارة أحمد",
        releaseDate: LocalDate? = LocalDate(2026, 5, 4),
        language: String? = "ar",
    ): ContentItem.AudioBook = ContentItem.AudioBook(
        id = id,
        title = title,
        imageUrl = imageUrl,
        durationSeconds = durationSeconds,
        authorName = authorName,
        releaseDate = releaseDate,
        language = language,
    )

    fun audioArticle(
        id: String = "art-1",
        title: String = "تحليل سوق اليوم",
        imageUrl: String? = null,
        durationSeconds: Long? = 900L,
        authorName: String? = "محرر الاقتصاد",
        releaseDate: LocalDate? = LocalDate(2026, 5, 1),
    ): ContentItem.AudioArticle = ContentItem.AudioArticle(
        id = id,
        title = title,
        imageUrl = imageUrl,
        durationSeconds = durationSeconds,
        authorName = authorName,
        releaseDate = releaseDate,
    )

    fun unknown(
        id: String = "unk-1",
        title: String = "Unknown payload",
        imageUrl: String? = sampleImages[0],
        durationSeconds: Long? = 60L,
        raw: Map<String, String> = mapOf("type" to "custom"),
    ): ContentItem.UnknownContent = ContentItem.UnknownContent(
        id = id,
        title = title,
        imageUrl = imageUrl,
        durationSeconds = durationSeconds,
        raw = raw,
    )

    /** Single representative of each [ContentItem] subtype, with intentional nulls/mix. */
    fun defaultList(): List<ContentItem> = listOf(
        podcast(),
        episode(),
        episodeEnglish(),
        audioBook(),
        audioArticle(),
        unknown(),
    )

    /** Items suited for queue deck rows (overlapping thumbnails from neighbors). */
    fun queueRowNeighbors(): List<ContentItem> = listOf(
        episode(id = "q1", title = "الحلقة الأولى", imageUrl = sampleImages[0], durationSeconds = 1380L, releaseDate = LocalDate(2026, 5, 9)),
        episode(id = "q2", title = "الحلقة الثانية", imageUrl = sampleImages[1], durationSeconds = 2100L, releaseDate = LocalDate(2026, 5, 8)),
        episode(id = "q3", title = "الحلقة الثالثة", imageUrl = sampleImages[2], durationSeconds = 900L, releaseDate = LocalDate(2026, 5, 7)),
        episode(id = "q4", title = "الحلقة الرابعة", imageUrl = sampleImages[3], durationSeconds = 1800L, releaseDate = LocalDate(2026, 5, 6)),
    )
}

object HomeSectionSamples {

    fun squareSection(): HomeSection = HomeSection(
        id = "sec-square",
        title = "موصى به لك",
        layout = SectionLayout.Square,
        contentType = ContentType.Podcast,
        order = 0,
        items = listOf(
            ContentItemSamples.podcast(id = "sq1", title = "بودكاست أ"),
            ContentItemSamples.podcast(id = "sq2", title = "بودكاست ب", imageUrl = ContentItemSamples.queueRowNeighbors()[1].imageUrl),
            ContentItemSamples.episode(id = "sq3", title = "حلقة مقترحة", durationSeconds = 1800L),
        ),
    )

    fun twoLinesGridSection(): HomeSection = HomeSection(
        id = "sec-grid",
        title = "الحلقات الجديدة",
        layout = SectionLayout.TwoLinesGrid,
        contentType = ContentType.Episode,
        order = 1,
        items = listOf(
            ContentItemSamples.episode(),
            ContentItemSamples.episodeEnglish(),
            ContentItemSamples.episode(
                id = "ep-3",
                title = "د. فيصل الرفاعي | هل استطاع الإنسان مواكبة التطور؟",
                durationSeconds = 5400L,
                releaseDate = LocalDate(2026, 5, 9),
            ),
            ContentItemSamples.episode(
                id = "ep-grid-4",
                title = "السفر الصامت وضوضاء الإعلانات",
                durationSeconds = 1380L,
                releaseDate = LocalDate(2026, 5, 8),
            ),
            ContentItemSamples.episode(
                id = "ep-grid-5",
                title = "حلقة خمس",
                durationSeconds = 2100L,
                imageUrl = "https://picsum.photos/seed/alfarouk-c/400/400",
            ),
            ContentItemSamples.episode(
                id = "ep-grid-6",
                title = "حلقة ست",
                durationSeconds = 900L,
            ),
        ),
    )

    fun bigSquareSection(): HomeSection = HomeSection(
        id = "sec-big",
        title = "اسمع قبل الناس",
        layout = SectionLayout.BigSquare,
        contentType = ContentType.Podcast,
        order = 2,
        items = listOf(
            ContentItemSamples.podcast(id = "bs1", title = "إذا ودك تضحك", popularityScore = 8, episodeCount = 10),
            ContentItemSamples.podcast(
                id = "bs2",
                title = "بولندا من الفقر إلى الغنى",
                imageUrl = ContentItemSamples.queueRowNeighbors()[1].imageUrl,
                episodeCount = 24,
                popularityScore = 8,
            ),
        ),
    )

    fun queueSection(): HomeSection = HomeSection(
        id = "sec-queue",
        title = "الطابور",
        layout = SectionLayout.Queue,
        contentType = ContentType.Episode,
        order = 3,
        items = ContentItemSamples.queueRowNeighbors(),
    )

    fun loungeSection(): HomeSection = HomeSection(
        id = "sec-lounge",
        title = "من الصالة",
        layout = SectionLayout.Lounge,
        contentType = ContentType.AudioArticle,
        order = 4,
        items = listOf(
            ContentItemSamples.audioArticle(
                id = "lg1",
                title = "عنوان منشور طويل يشرح محتوى الحلقة المرفقة",
                authorName = "عبدالرحمن",
                releaseDate = LocalDate(2026, 5, 9),
            ),
            ContentItemSamples.audioArticle(
                id = "lg2",
                title = "منشور ثانٍ للمعاينة الأفقية",
                authorName = "سارة",
                releaseDate = LocalDate(2026, 5, 8),
            ),
        ),
    )

    fun unknownSection(): HomeSection = HomeSection(
        id = "sec-unknown",
        title = "قسم تجريبي",
        layout = SectionLayout.Unknown,
        contentType = ContentType.Unknown,
        order = 99,
        items = listOf(
            ContentItemSamples.unknown(),
            ContentItemSamples.unknown(
                id = "unknown-2",
                title = "عنصر غير معروف آخر",
            ),
        ),
    )

    fun defaultFeed(): List<HomeSection> = listOf(
        squareSection(),
        twoLinesGridSection(),
        bigSquareSection(),
        queueSection(),
        loungeSection(),
    )
}

package com.shiv_ndroid98.cinebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jgabrielfreitas.core.BlurImageView;

import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static android.view.View.Y;
import static java.lang.System.load;

public class Information extends YouTubeBaseActivity {
	ImageView img;
	TextView title, plot;
	TextView release_date, duration, genre, o_title, o_lang, budget;
	String t, id;

	YouTubePlayerView youplay;
	YouTubePlayer.OnInitializedListener onInitializedListener;
	public static final String KEY = "AIzaSyA1hqubKfdYkkZ-40HWr8ny77300ZZ1JwI";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);
		img = findViewById(R.id.poster);
		plot = findViewById(R.id.plot);
		release_date = findViewById(R.id.year);
		duration = findViewById(R.id.duration);
		genre = findViewById(R.id.genre);
		title = findViewById(R.id.mov_title);
		o_title = findViewById(R.id.original_title);
		o_lang = findViewById(R.id.original_lang);
		budget = findViewById(R.id.Budget);

		t = getIntent().getStringExtra("title");
		id = getIntent().getStringExtra("id");

		youplay = findViewById(R.id.video1);

		makeTextViewResizable(plot, 3, "View More", true);


		//String moreinfo = "http://www.omdbapi.com/?i="+imdbid+"&plot=full&apikey=d8395802";
		final String moreinfo = "http://api.themoviedb.org/3/movie/" + id + "?api_key=60e3c97f405f942ade7455b8d5a6993f";
		String videoapi = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=60e3c97f405f942ade7455b8d5a6993f";
		//  final ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoading);
		// pb.setVisibility(ProgressBar.VISIBLE);

		//AIzaSyA1hqubKfdYkkZ-40HWr8ny77300ZZ1JwI   --- YOUTUBE API (Trailer)
		try {
			StringRequest Videorequest = new StringRequest(videoapi, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					Log.d("CODE", response);

					try {
						GsonBuilder gsonBuilder = new GsonBuilder();
						Gson gson = gsonBuilder.create();

						// pb.setVisibility(ProgressBar.INVISIBLE);

						MovieTrailer movie_trailer = gson.fromJson(response, MovieTrailer.class);

						List<VideoResults> al = movie_trailer.getResults();

						VideoResults[] movies = new VideoResults[al.size()];
						al.toArray(movies);

						final String videokey = movies[0].getKey();


						onInitializedListener = new YouTubePlayer.OnInitializedListener() {
							@Override
							public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

								youTubePlayer.cueVideo(videokey);
							}

							@Override
							public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

							}
						};

						youplay.initialize(KEY, onInitializedListener);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(Information.this, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
				}
			});

			RequestQueue vqueue = Volley.newRequestQueue(this);
			vqueue.add(Videorequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			StringRequest request = new StringRequest(moreinfo, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					Log.d("CODE", response);

					try {
						GsonBuilder gsonBuilder = new GsonBuilder();
						Gson gson = gsonBuilder.create();

						//    pb.setVisibility(ProgressBar.INVISIBLE);

						TMDBMovieInfo movieinfo = gson.fromJson(response, TMDBMovieInfo.class);
						//Glide.with(img.getContext()).load(movieinfo.getPoster()).into(img);

						title.setText(t);
						release_date.setText(movieinfo.getReleaseDate());

						String dur = movieinfo.getRuntime().toString();
						String k1 = "Budget " + dur;
						budget.setText(k1);

						//duration.setText(movieinfo.getRuntime());
						// genre.setText(movieinfo.get);

						o_title.setText(movieinfo.getOriginalTitle());
						o_lang.setText(movieinfo.getOriginalLanguage());
						String budg = movieinfo.getBudget().toString();
						String k2 = "Budget " + budg;
						budget.setText(k2);


						plot.setText(movieinfo.getOverview());

						String posterPath = "http://image.tmdb.org/t/p/w780/" + movieinfo.getPosterPath();
						Glide.with(img.getContext()).load(posterPath).into(img);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(Information.this, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
				}
			});

			RequestQueue queue = Volley.newRequestQueue(this);
			queue.add(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

		if (tv.getTag() == null) {
			tv.setTag(tv.getText());
		}
		ViewTreeObserver vto = tv.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				String text;
				int lineEndIndex;
				ViewTreeObserver obs = tv.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				if (maxLine == 0) {
					lineEndIndex = tv.getLayout().getLineEnd(0);
					text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
				} else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
					lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
					text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
				} else {
					lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
					text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
				}
				tv.setText(text);
				tv.setMovementMethod(LinkMovementMethod.getInstance());
				tv.setText(
						addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
								viewMore), TextView.BufferType.SPANNABLE);
			}
		});

	}

	private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
	                                                                        final int maxLine, final String spanableText, final boolean viewMore) {
		String str = strSpanned.toString();
		SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

		if (str.contains(spanableText)) {


			ssb.setSpan(new MySpannable(false) {
				@Override
				public void onClick(View widget) {
					tv.setLayoutParams(tv.getLayoutParams());
					tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
					tv.invalidate();
					if (viewMore) {
						makeTextViewResizable(tv, -1, "View Less", false);
					} else {
						makeTextViewResizable(tv, 3, "View More", true);
					}
				}
			}, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

		}
		return ssb;

	}

}







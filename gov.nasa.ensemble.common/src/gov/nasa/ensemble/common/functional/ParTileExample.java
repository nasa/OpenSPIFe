/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.common.functional;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.time.StopWatch;

import fj.Effect;
import fj.F;
import fj.P;
import fj.P2;
import fj.Unit;
import fj.control.parallel.Actor;
import fj.control.parallel.ParModule;
import fj.control.parallel.Promise;
import fj.control.parallel.Strategy;
import fj.data.List;
import fj.data.Tree;
import fj.data.vector.V;
import fj.data.vector.V2;
import gov.nasa.ensemble.common.thread.ThreadUtils;


public class ParTileExample {

	private static final double IMAGE_WIDTH = Math.pow(2, 15);
	private static final double IMAGE_HEIGHT = IMAGE_WIDTH;
	private static final int SCALE_TIME = 10;
	private static final int SAVE_TIME = 50;
	private static final int NUM_THREADS = 100;

	private static class Image {
		public final V2<Double> size;
		
		public Image(V2<Double> size) {
			this.size = size;
		}
	}
	
	private static class Tile {
		public final Image data;
		public final V2<Double> loc;
		public final V2<Double> size;
		
		public Tile(Image data, V2<Double> loc, V2<Double> size) {
			this.data = data;
			this.loc = loc;
			this.size = size;
		}
		
		@Override
		public String toString() {
			return "(" + loc._1().intValue() + ", " + loc._2().intValue() + ", " + 
						 size._1().intValue() + ", " + size._2().intValue() + ")";
		}

		public static F<Tree<Tile>, Image> toImage() {
			return new F<Tree<Tile>, Image>() {
				@Override
				public Image f(final Tree<Tile> tree) {
					return tree.root().data;
				}
			};
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		final ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
		final ParModule pm = ParModule.parModule(Strategy.<Unit>executorStrategy(pool));
		
		final Actor<Tree<Tile>> tileWriter = pm.effect(new Effect<Tree<Tile>>() {
			@Override
			public void e(Tree<Tile> tree) {
				final List<Tile> nodes = tree.flatten().toList();
				final Actor<String> callback = pm.actor(new Effect<String>() {
					final int totalTiles = nodes.length();
					int counter = 0;
					@Override
					public void e(final String response) {
//						System.err.println(response);
						if (++counter >= totalTiles) {
							final String msg = MessageFormat.format(
								"All done! Made {0} tiles for a {1} pixel image in {2} seconds", 
								counter, IMAGE_WIDTH * IMAGE_HEIGHT, stopWatch.getTime() / 1000.0);
							System.err.println(msg);
							pool.shutdown();
						}
					}
				}).asActor();
				
				nodes.foreach(Actors.act(pm.effect(new Effect<Tile>() {
					@Override
					public void e(final Tile tile) {
						ThreadUtils.sleep(SAVE_TIME);
						callback.act("done saving " + tile);
					}
				})));
			}
		});
		
		final Image inputImage = new Image(V.v(IMAGE_WIDTH, IMAGE_HEIGHT));
		
		process(inputImage, V.v(0.0, 0.0), 0, pm).to(tileWriter);
	}

	private static Promise<Tree<Tile>> process(final Image image, final V2<Double> loc, final int depth, final ParModule pm) {
		final List<P2<Image, V2<Double>>> childImages = quarter(image, loc);
		final List<Promise<Tree<Tile>>> childPromises = 
			childImages.map(new F<P2<Image, V2<Double>>, Promise<Tree<Tile>>>() {
				@Override
				public Promise<Tree<Tile>> f(final P2<Image, V2<Double>> childInput) {
					return process(childInput._1(), childInput._2(), depth + 1, pm);
				}
			});
		
		return pm.sequence(childPromises).fmap(new F<List<Tree<Tile>>, Tree<Tile>>() {
			@Override
			public Tree<Tile> f(final List<Tree<Tile>> children) {
				final Tile tile = new Tile(coalesce(children.map(Tile.toImage())), loc, image.size);
				return Tree.node(tile, children);
			}
		});
	}
	
	private static Image coalesce(List<Image> childImages) {
		ThreadUtils.sleep(SCALE_TIME);
		return new Image(V.v(256.0, 256.0));
	}

	public static List<P2<Image, V2<Double>>> quarter(Image image, V2<Double> loc) {
		if (image.size._1() <= 256 && image.size._2() <= 256)
			return List.nil();
		final V2<Double> newSize = image.size.map(FMath.scale(.5));
		final Double locX = loc._1();
		final Double locY = loc._2();
		return List.list(P.p(new Image(newSize), V.v(locX, locY)),
						 P.p(new Image(newSize), V.v(locX + newSize._1(), locY)),
						 P.p(new Image(newSize), V.v(locX, locY + newSize._2())),
						 P.p(new Image(newSize), V.v(locX + newSize._1(), locY + newSize._2())));
	}
}
